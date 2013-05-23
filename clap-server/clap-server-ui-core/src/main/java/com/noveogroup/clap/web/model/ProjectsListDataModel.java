package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.Project;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ProjectsListDataModel extends ListDataModel<Project> implements SelectableDataModel<Project> {

    public ProjectsListDataModel() {
    }

    public ProjectsListDataModel(final List<Project> projects) {
        super(projects);
    }

    @Override
    public Object getRowKey(final Project project) {
        return project.getId();
    }

    @Override
    public Project getRowData(final String s) {
        final long id = Long.parseLong(s);
        final List<Project> projects = (List<Project>) getWrappedData();
        for (final Project project : projects) {
            if (id == project.getId()) {
                return project;
            }
        }
        return null;
    }
}
