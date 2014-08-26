package com.noveogroup.clap.web.model.projects;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ProjectsModel implements Serializable {

    private Project newProject = new Project();


    private ImagedProject selectedProject;

    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(final Project newProject) {
        this.newProject = newProject;
    }


    public ImagedProject getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(final ImagedProject selectedProject) {
        this.selectedProject = selectedProject;
    }
}
