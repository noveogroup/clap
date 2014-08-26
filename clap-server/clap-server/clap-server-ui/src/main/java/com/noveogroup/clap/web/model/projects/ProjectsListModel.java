package com.noveogroup.clap.web.model.projects;

import com.noveogroup.clap.model.project.ImagedProject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class ProjectsListModel {

    private List<ImagedProject> projectList;

    public List<ImagedProject> getProjectList() {
        return projectList;
    }

    public void setProjectList(final List<ImagedProject> projectList) {
        this.projectList = projectList;
    }
}
