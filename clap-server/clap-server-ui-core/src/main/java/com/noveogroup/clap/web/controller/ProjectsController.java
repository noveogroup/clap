package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsListDataModel;
import com.noveogroup.clap.web.model.ProjectsModel;
import com.noveogroup.clap.web.model.RevisionsListDataModel;
import com.noveogroup.clap.web.model.RevisionsModel;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ProjectsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    private long projectId;

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    public String addProject() {
        try {
            LOGGER.debug("add project : " + projectsModel.getNewProject());
            projectService.createProject(projectsModel.getNewProject());
            projectsModel.setNewProject(new Project());
            LOGGER.debug("project saved");
            return Navigation.PROJECTS.getView();
        } catch (Exception e){
            final FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Error while creating project");
            FacesContext.getCurrentInstance().addMessage(null,message);
            return Navigation.SAME_PAGE.getView();
        }
    }

    public void prepareProjectsListView() {
        final List<Project> projectList = projectService.findAllProjects();
        projectsModel.setProjectsListDataModel(new ProjectsListDataModel(projectList));
        LOGGER.debug(projectList.size() + " projects loaded");
    }

    public void prepareProjectView(){
        final Project selectedProject = projectsModel.getSelectedProject();
        if(selectedProject != null){
            final Project projectWithRevisions = projectService.findById(selectedProject.getId());
            projectsModel.setSelectedProject(projectWithRevisions);
            revisionsModel.setRevisionsListDataModel(new RevisionsListDataModel(projectWithRevisions.getRevisions()));
        }
        else {
            LOGGER.error("project not selected");
        }
    }


    public void onProjectSelect(final SelectEvent event) {
        final Project selectedProject = (Project) event.getObject();
        projectsModel.setSelectedProject(selectedProject);
        LOGGER.debug(selectedProject.getName() + " prject selected");
        redirectTo(Navigation.PROJECT);
    }


}