package com.noveogroup.clap.web.model.projects;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class ProjectsModel implements Serializable {

    private Project newProject = new Project();

    private List<ImagedProject> projectList;

    private ImagedProject selectedProject;

    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(final Project newProject) {
        this.newProject = newProject;
    }

    public List<ImagedProject> getProjectList() {
        return projectList;
    }

    public void setProjectList(final List<ImagedProject> projectList) {
        this.projectList = projectList;
    }

    public ImagedProject getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(final ImagedProject selectedProject) {
        this.selectedProject = selectedProject;
    }
}
