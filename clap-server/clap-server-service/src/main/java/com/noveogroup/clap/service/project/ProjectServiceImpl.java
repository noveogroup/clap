package com.noveogroup.clap.service.project;

import com.google.common.collect.Lists;
import com.noveogroup.clap.converter.ProjectConverter;
import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.service.url.UrlService;
import com.noveogroup.clap.service.user.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @Inject
    private UrlService urlService;

    @Inject
    private UserService userService;

    private ProjectConverter projectConverter = new ProjectConverter();

    @WrapException
    @RequiresAuthentication
    @Override
    public Project createProject(final Project project) {
        if (project.getCreationDate() == null) {
            project.setCreationDate(new Date());
        }
        final ProjectEntity projectEntity = projectConverter.map(project);
        return projectConverter.map(projectDAO.persist(projectEntity));
    }

    @WrapException
    @RequiresAuthentication
    @RequiresPermissions("EDIT_PROJECTS")
    @Override
    public Project save(final Project project) {
        final ProjectEntity projectEntity = projectConverter.map(project);
        return projectConverter.map(projectDAO.persist(projectEntity));
    }

    @RequiresAuthentication
    @Override
    public Project findById(final Long id) {
        final ProjectEntity projectEntity = projectDAO.findById(id);
        final Project project = projectConverter.map(projectEntity);
        updateRevisionUrls(id,projectEntity,project);
        return project;
    }

    @RequiresAuthentication
    @Override
    public ImagedProject findByIdWithImage(final Long id) {
        final ProjectEntity projectEntity = projectDAO.findById(id);
        final ImagedProject project = projectConverter.mapToImagedProject(projectEntity);
        updateRevisionUrls(id,projectEntity,project);
        return project;
    }

    @RequiresAuthentication
    @Override
    public List<Project> findAllProjects() {
        final List<ProjectEntity> projectEntityList = projectDAO.selectAll();
        final List<Project> projectList = Lists.newArrayList();
        for (final ProjectEntity projectEntity : projectEntityList) {
            projectList.add(projectConverter.map(projectEntity, false));
        }
        return projectList;
    }

    @RequiresAuthentication
    @Override
    public List<ImagedProject> findAllImagedProjects() {
        final List<ProjectEntity> projectEntityList = projectDAO.selectAll();
        final List<ImagedProject> projectList = Lists.newArrayList();
        for (final ProjectEntity projectEntity : projectEntityList) {
            projectList.add(projectConverter.mapToImagedProject(projectEntity, false));
        }
        return projectList;
    }


    private void updateRevisionUrls(final Long id,
                                    final ProjectEntity projectEntity,
                                    final Project project) {
        final UserWithPersistedAuth userWithToken = userService.getUserWithToken();
        final String token = userWithToken.getToken();
        final List<Revision> revisions = project.getRevisions();
        for (int i = 0; i < revisions.size(); i++) {
            final Revision revision = revisions.get(i);
            final RevisionEntity revisionEntityOrigin = projectEntity.getRevisions().get(i);
            revision.setProjectId(project.getId());
            if (revisionEntityOrigin.isMainPackageLoaded()) {
                revision.setMainPackageUrl(urlService.createUrl(revision.getId(), true, token));
            }
            if (revisionEntityOrigin.isSpecialPackageLoaded()) {
                revision.setSpecialPackageUrl(urlService.createUrl(revision.getId(), false, token));
            }
        }
    }


    public void setProjectConverter(final ProjectConverter projectConverter) {
        this.projectConverter = projectConverter;
    }
}
