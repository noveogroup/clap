package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.ProjectDTO;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ProjectsListDataModel extends ListDataModel<ProjectDTO> implements SelectableDataModel<ProjectDTO> {

    public ProjectsListDataModel() {
    }

    public ProjectsListDataModel(List<ProjectDTO> projectDTOs) {
        super(projectDTOs);
    }

    @Override
    public Object getRowKey(ProjectDTO projectDTO) {
        return projectDTO.getId();
    }

    @Override
    public ProjectDTO getRowData(String s) {
        final long id = Long.parseLong(s);
        List<ProjectDTO> projectDTOs = (List<ProjectDTO>) getWrappedData();
        for (ProjectDTO projectDTO : projectDTOs) {
            if (id == projectDTO.getId()) {
                return projectDTO;
            }
        }
        return null;
    }
}
