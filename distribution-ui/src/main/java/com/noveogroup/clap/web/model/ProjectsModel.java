package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.ProjectDTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProjectsModel implements Serializable{

    private ProjectDTO newProject = new ProjectDTO();

    private ProjectsListDataModel projectsListDataModel;

    private ProjectDTO selectedProject;

    public ProjectDTO getNewProject() {
        return newProject;
    }

    public void setNewProject(ProjectDTO newProject) {
        this.newProject = newProject;
    }

    public ProjectsListDataModel getProjectsListDataModel() {
        return projectsListDataModel;
    }

    public void setProjectsListDataModel(ProjectsListDataModel projectsListDataModel) {
        this.projectsListDataModel = projectsListDataModel;
    }

    public ProjectDTO getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(ProjectDTO selectedProject) {
        this.selectedProject = selectedProject;
    }
}
