package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.exception.ClapPersistenceException;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.projects.ProjectsListModel;
import com.noveogroup.clap.web.model.projects.ProjectsModel;
import com.noveogroup.clap.web.model.revisions.RevisionsListDataModel;
import com.noveogroup.clap.web.model.revisions.RevisionsModel;
import com.noveogroup.clap.web.util.message.MessageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
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
    private ProjectsListModel projectsListModel;

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
        LOGGER.debug("call project service");
        final List<ImagedProject> projectList = projectService.findAllImagedProjects();
        projectsListModel.setProjectList(projectList);
        LOGGER.debug("project service ret " + projectList);
    }

    public void prepareProjectView() {
        final Project selectedProject = projectsModel.getSelectedProject();
        if (selectedProject != null) {
            revisionsModel.setRevisionsListDataModel(
                    new RevisionsListDataModel(selectedProject.getRevisions()));
        } else {
            messageSupport.addMessage(null,
                    new FacesMessage(messageSupport.getMessage("error.badRequest.project",
                            new Object[]{"no project id"})));
            LOGGER.error("project not selected");
        }
    }

    public String toEditProjectView() {
        return Navigation.EDIT_PROJECT.getView();
    }

    public String toAddProjectView() {
        return Navigation.ADD_PROJECT.getView();
    }

}
