package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsListDataModel;
import com.noveogroup.clap.web.model.ProjectsModel;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ProjectsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectsModel projectsModel;

    public String addProject(){

        LOGGER.debug("add project : " + projectsModel.getNewProject());

        projectService.save(projectsModel.getNewProject());
        projectsModel.setNewProject(new ProjectDTO());
        LOGGER.debug("project saved");

        return toProjectsView();
    }

    public String toProjectsView(){
        List<ProjectDTO> projectDTOList = projectService.findAllProjects();
        projectsModel.setProjectsListDataModel(new ProjectsListDataModel(projectDTOList));
        LOGGER.debug(projectDTOList.size() + " projects loaded");
        return Navigation.PROJECTS.getView();
    }

    public void onProjectSelect(SelectEvent event){
        ProjectDTO selectedProject = projectsModel.getSelectedProject();
        if(selectedProject != null){
            LOGGER.debug(selectedProject.getName() + " prject selected");
            ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            configurableNavigationHandler.performNavigation(Navigation.PROJECT.getView());
        } else {
            LOGGER.error("no projects selected");
        }
    }
}
