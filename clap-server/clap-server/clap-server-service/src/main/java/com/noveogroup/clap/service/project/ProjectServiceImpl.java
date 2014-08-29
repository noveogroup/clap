package com.noveogroup.clap.service.project;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.ProjectConverter;
import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.exception.ClapDataIntegrityException;
import com.noveogroup.clap.exception.ClapPersistenceException;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionVariant;
import com.noveogroup.clap.model.user.User;
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

    @EJB
    private UserDAO userDAO;

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
        if (projectEntity != null) {
            final Project project = projectConverter.map(projectEntity);
            updateRevisionUrls(id, projectEntity, project);
            return project;
        } else {
            return null;
        }
    }

    @RequiresAuthentication
    @Override
    public ImagedProject findByIdWithImage(final Long id) {
        final ProjectEntity projectEntity = projectDAO.findById(id);
        if (projectEntity != null) {
            final ImagedProject project = mapToImagedProject(id, projectEntity, true);
            return project;
        } else {
            return null;
        }
    }

    private ImagedProject mapToImagedProject(final Long id,
                                             final ProjectEntity projectEntity,
                                             final boolean mapRevisions) {
        final ImagedProject project = projectConverter.mapToImagedProject(projectEntity, configBean, mapRevisions);
        updateProjectWatched(project);
        if (mapRevisions) {
            updateRevisionUrls(id, projectEntity, project);
        }
        return project;
    }

    private void updateProjectWatched(final ImagedProject imagedProject) {
        final User user = userService.getUser();
        if (user != null) {
            final List<Project> watchedProjects = user.getWatchedProjects();
            if (watchedProjects != null) {
                for (Project project : watchedProjects) {
                    if (imagedProject.getId() == project.getId()) {
                        imagedProject.setWatched(true);
                        return;
                    }
                }
            }
            imagedProject.setWatched(false);
        }
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
            final ImagedProject imagedProject = mapToImagedProject(projectEntity.getId(), projectEntity, false);
            projectList.add(imagedProject);
        }
        return projectList;
    }

    @WrapException
    @RequiresAuthentication
    @RequiresPermissions("DELETE_PROJECTS")
    @Override
    public void deleteProject(final Project project) {
        projectDAO.removeById(project.getId());
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


    @Override
    public ImagedProject watchProject(final long projectId) {
        final String currentUserLogin = userService.getCurrentUserLogin();
        if (currentUserLogin != null) {
            final UserEntity userByLogin = userDAO.getUserByLogin(currentUserLogin);
            List<ProjectEntity> watchedProjects = userByLogin.getWatchedProjects();
            if (watchedProjects == null) {
                watchedProjects = Lists.newArrayList();
                userByLogin.setWatchedProjects(watchedProjects);
            }
            final ProjectEntity projectEntity = projectDAO.findById(projectId);
            if (projectEntity != null) {
                if (watchedProjects.contains(projectEntity)) {
                    watchedProjects.remove(projectEntity);
                } else {
                    watchedProjects.add(projectEntity);
                }
                userDAO.persist(userByLogin);
                return mapToImagedProject(projectId, projectEntity, true);
            } else {
                throw new ClapDataIntegrityException("no project with id = " + projectId);
            }
        } else {
            throw new ClapDataIntegrityException("no current user");
        }
    }

    public void setProjectConverter(final ProjectConverter projectConverter) {
        this.projectConverter = projectConverter;
    }
}
