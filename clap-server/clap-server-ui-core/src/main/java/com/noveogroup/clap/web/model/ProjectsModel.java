package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.Project;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ProjectsModel implements Serializable {

    private Project newProject = new Project();

    private ProjectsListDataModel projectsListDataModel;

    private StreamedImagedProject selectedProject;

    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(final Project newProject) {
        this.newProject = newProject;
    }

    public ProjectsListDataModel getProjectsListDataModel() {
        return projectsListDataModel;
    }

    public void setProjectsListDataModel(final ProjectsListDataModel projectsListDataModel) {
        this.projectsListDataModel = projectsListDataModel;
    }

    public StreamedImagedProject getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(final StreamedImagedProject selectedProject) {
        this.selectedProject = selectedProject;
    }

}
