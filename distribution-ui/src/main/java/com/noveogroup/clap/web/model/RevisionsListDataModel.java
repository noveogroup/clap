package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.revision.Revision;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class RevisionsListDataModel extends ListDataModel<Revision> implements SelectableDataModel<Revision> {

    public RevisionsListDataModel() {
    }

    public RevisionsListDataModel(List<Revision> revisions) {
        super(revisions);
    }

    @Override
    public Object getRowKey(Revision revision) {
        return revision.getId();
    }

    @Override
    public Revision getRowData(String rowKey) {
        final long id = Long.parseLong(rowKey);
        List<Revision> revisions = (List<Revision>) getWrappedData();
        for (Revision revision : revisions) {
            if (id == revision.getId()) {
                return revision;
            }
        }
        return null;
    }
}
