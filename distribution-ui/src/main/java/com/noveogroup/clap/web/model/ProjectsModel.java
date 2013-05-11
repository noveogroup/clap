package com.noveogroup.clap.web.model;

import com.noveogroup.clap.entity.Project;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProjectsModel implements Serializable{

    private Project newProject = new Project();

    private List<Project> projects;

    private Project selectedProject;

    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(Project newProject) {
        this.newProject = newProject;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }
}
