package com.noveogroup.clap.web.model.projects;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ProjectsListDataModel extends ListDataModel<StreamedImagedProject>
        implements SelectableDataModel<StreamedImagedProject> {

    public ProjectsListDataModel() {
    }

    public ProjectsListDataModel(final List<StreamedImagedProject> projects) {
        super(projects);
    }

    @Override
    public Object getRowKey(final StreamedImagedProject project) {
        return project.getId();
    }

    @Override
    public StreamedImagedProject getRowData(final String s) {
        final long id = Long.parseLong(s);
        final List<StreamedImagedProject> projects = (List<StreamedImagedProject>) getWrappedData();
        for (final StreamedImagedProject project : projects) {
            if (id == project.getId()) {
                return project;
            }
        }
        return null;
    }
}
