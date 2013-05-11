package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.revision.RevisionDTO;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class RevisionsListDataModel extends ListDataModel<RevisionDTO> implements SelectableDataModel<RevisionDTO> {

    public RevisionsListDataModel() {
    }

    public RevisionsListDataModel(List<RevisionDTO> revisionDTOs) {
        super(revisionDTOs);
    }

    @Override
    public Object getRowKey(RevisionDTO revisionDTO) {
        return revisionDTO.getId();
    }

    @Override
    public RevisionDTO getRowData(String rowKey) {
        final long id = Long.parseLong(rowKey);
        List<RevisionDTO> revisionDTOs = (List<RevisionDTO>) getWrappedData();
        for (RevisionDTO revisionDTO : revisionDTOs) {
            if (id == revisionDTO.getId()) {
                return revisionDTO;
            }
        }
        return null;
    }
}
