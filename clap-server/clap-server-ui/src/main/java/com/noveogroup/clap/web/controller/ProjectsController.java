package com.noveogroup.clap.web.controller;

import com.google.common.collect.Lists;
import com.noveogroup.clap.exception.ClapPersistenceException;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.projects.ProjectsListDataModel;
import com.noveogroup.clap.web.model.projects.ProjectsModel;
import com.noveogroup.clap.web.model.projects.StreamedImagedProject;
import com.noveogroup.clap.web.model.revisions.RevisionsListDataModel;
import com.noveogroup.clap.web.model.revisions.RevisionsModel;
import com.noveogroup.clap.web.util.message.MessageSupport;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ProjectsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    private long projectId;

    @Inject
    private MessageSupport messageSupport;

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    public String addProject() {
        LOGGER.debug("add project : " + projectsModel.getNewProject());
        try {
            projectService.createProject(projectsModel.getNewProject());
        } catch (ClapPersistenceException e) {
            throw new ClapPersistenceException(messageSupport.getMessage("project.add.message.exists"), e.getCause());
        }
        projectsModel.setNewProject(new Project());
        LOGGER.debug("project saved");
        return Navigation.PROJECTS.getView();
    }

    public String editProject() {
        projectService.save(projectsModel.getSelectedProject());
        return Navigation.PROJECT.getView();
    }

    public String deleteProject() {
        projectService.deleteProject(projectsModel.getSelectedProject());
        return Navigation.PROJECTS.getView();
    }

    public void prepareProjectsListView() {
        if (isAjaxRequest()) {
            return;
        }
        LOGGER.debug("call project service");
        final List<ImagedProject> projectList = projectService.findAllImagedProjects();
        LOGGER.debug("project service ret " + projectList);
        if (projectList != null) {
            final List<StreamedImagedProject> streamedImagedProjects = Lists.newArrayList();
            for (final ImagedProject project : projectList) {
                streamedImagedProjects.add(new StreamedImagedProject(project));
            }
            projectsModel.setProjectsListDataModel(new ProjectsListDataModel(streamedImagedProjects));
            LOGGER.debug(projectList.size() + " projects loaded");
        }
    }

    public void prepareProjectView() {
        if (isAjaxRequest()) {
            return;
        }
        final Project selectedProject = projectsModel.getSelectedProject();
        if (selectedProject != null) {
            final ImagedProject projectWithRevisions = projectService.findByIdWithImage(selectedProject.getId());
            if (projectWithRevisions != null) {
                projectsModel.setSelectedProject(new StreamedImagedProject(projectWithRevisions));
                revisionsModel.setRevisionsListDataModel(
                        new RevisionsListDataModel(projectWithRevisions.getRevisions()));
            }
        } else {
            LOGGER.error("project not selected");
        }
    }

    public void setSelectedProject(final Long projectId) {
        final StreamedImagedProject selectedProject = projectsModel.getSelectedProject();
        if (selectedProject == null || !projectId.equals(selectedProject.getId())) {
            final ImagedProject projectWithRevisions = projectService.findByIdWithImage(projectId);
            if (projectWithRevisions != null) {
                projectsModel.setSelectedProject(new StreamedImagedProject(projectWithRevisions));
                revisionsModel.setRevisionsListDataModel(
                        new RevisionsListDataModel(projectWithRevisions.getRevisions()));
            }
        }
    }


    public void onProjectSelect(final SelectEvent event) {
        final StreamedImagedProject selectedProject = (StreamedImagedProject) event.getObject();
        projectsModel.setSelectedProject(selectedProject);
        LOGGER.debug(selectedProject.getName() + " project selected");
        redirectTo(Navigation.PROJECT);
    }

    public String toEditProjectView() {
        return Navigation.EDIT_PROJECT.getView();
    }

    public String toAddProjectView() {
        return Navigation.ADD_PROJECT.getView();
    }

}
