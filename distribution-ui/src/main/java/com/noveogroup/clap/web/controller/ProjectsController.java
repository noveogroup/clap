package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.service.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

//        projectService.save(projectsModel.getNewProject());
        projectsModel.setNewProject(new Project());
        LOGGER.debug("project saved");

        return toProjectsView();
    }

    public String toProjectsView(){

        //TODO init projects list

        LOGGER.debug(projectsModel.getProjects().size() + " projects loaded");
        return Navigation.PROJECTS.getView();
    }

    public String onProjectSelect(){
        Project selectedProject = projectsModel.getSelectedProject();
        if(selectedProject != null){
            LOGGER.debug(selectedProject.getName() + " prject selected");
            return Navigation.PROJECT.getView();
        } else {
            LOGGER.error("no projects selected");
            return null;
        }
    }
}
