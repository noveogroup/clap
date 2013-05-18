package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.Project;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ProjectsListDataModel extends ListDataModel<Project> implements SelectableDataModel<Project> {

    public ProjectsListDataModel() {
    }

    public ProjectsListDataModel(List<Project> projects) {
        super(projects);
    }

    @Override
    public Object getRowKey(Project project) {
        return project.getId();
    }

    @Override
    public Project getRowData(String s) {
        final long id = Long.parseLong(s);
        List<Project> projects = (List<Project>) getWrappedData();
        for (Project project : projects) {
            if (id == project.getId()) {
                return project;
            }
        }
        return null;
    }
}
