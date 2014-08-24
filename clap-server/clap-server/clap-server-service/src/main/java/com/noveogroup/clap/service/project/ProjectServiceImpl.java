package com.noveogroup.clap.service.project;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.ProjectConverter;
import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.exception.ClapPersistenceException;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionVariant;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.service.file.FileService;
import com.noveogroup.clap.service.revision.RevisionService;
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
import java.io.File;
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
    private RevisionService revisionService;

    @Inject
    private FileService fileService;

    @Inject
    private UserService userService;

    @Inject
    private ConfigBean configBean;

    private ProjectConverter projectConverter = new ProjectConverter();

    @WrapException
    @RequiresAuthentication
    @Override
    public Project createProject(final Project project) {
        if (project.getCreationDate() == null) {
            project.setCreationDate(new Date());
        }
        final ProjectEntity projectEntity = projectConverter.map(project);
        final Project ret = projectConverter.map(projectDAO.persist(projectEntity));
        projectDAO.flush();
        return ret;
    }

    @WrapException
    @RequiresAuthentication
    @RequiresPermissions("EDIT_PROJECTS")
    @Override
    public Project save(final Project project) {
        final ProjectEntity projectEntity = projectDAO.findProjectByExternalIdOrReturnNull(project.getExternalId());
        if (projectEntity != null) {
            projectConverter.updateEntity(project, projectEntity);
            final ProjectEntity persisted = projectDAO.persist(projectEntity);
            projectDAO.flush();
            final Project ret = projectConverter.map(persisted);
            return ret;
        } else {
            throw new ClapPersistenceException("Project doesn't exists");
        }
    }

    @RequiresAuthentication
    @Override
    public Project findById(final Long id) {
        final ProjectEntity projectEntity = projectDAO.findById(id);
        final Project project = projectConverter.map(projectEntity);
        updateRevisionUrls(id, projectEntity, project);
        return project;
    }

    @RequiresAuthentication
    @Override
    public ImagedProject findByIdWithImage(final Long id) {
        final ProjectEntity projectEntity = projectDAO.findById(id);
        final ImagedProject project = projectConverter.mapToImagedProject(projectEntity, configBean);
        updateRevisionUrls(id, projectEntity, project);
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
            projectList.add(projectConverter.mapToImagedProject(projectEntity, configBean, false));
        }
        return projectList;
    }

    @WrapException
    @RequiresAuthentication
    @RequiresPermissions("DELETE_PROJECTS")
    @Override
    public void deleteProject(final Project project) {
        final ProjectEntity projectEntity = projectDAO.findById(project.getId());
        for (final RevisionEntity revisionEntity : projectEntity.getRevisions()) {
            revisionService.deleteRevision(revisionEntity.getId());
        }
        projectDAO.removeById(project.getId());
        projectDAO.flush();
    }

    @Override
    public File getProjectIcon(final long id) {

        final ProjectEntity byId = projectDAO.findById(id);
        if (byId != null) {
            return fileService.getFile(byId.getIconFilePath());
        } else {
            return null;
        }
    }


    private void updateRevisionUrls(final Long id,
                                    final ProjectEntity projectEntity,
                                    final Project project) {
        final UserWithPersistedAuth userWithToken = userService.getUserWithToken();
        final String token = userWithToken.getToken();
        final List<Revision> revisions = project.getRevisions();
        if (revisions != null) {
            for (final Revision revision : revisions) {
                revision.setProjectId(project.getId());
                for (final RevisionVariant variant : revision.getVariants()) {
                    variant.setPackageUrl(urlService.createUrl(revision.getId(), variant.getId(), token));
                }
            }
        }
    }


    public void setProjectConverter(final ProjectConverter projectConverter) {
        this.projectConverter = projectConverter;
    }
}
