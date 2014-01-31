package com.noveogroup.clap.web.model.revisions;

import com.noveogroup.clap.model.revision.Revision;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class RevisionsListDataModel extends ListDataModel<Revision> implements SelectableDataModel<Revision> {


    public RevisionsListDataModel(final List<Revision> revisions) {
        super(revisions);
    }

    @Override
    public Object getRowKey(final Revision revision) {
        return revision.getId();
    }

    @Override
    public Revision getRowData(final String rowKey) {
        final long id = Long.parseLong(rowKey);
        final List<Revision> revisions = (List<Revision>) getWrappedData();
        for (final Revision revision : revisions) {
            if (id == revision.getId()) {
                return revision;
            }
        }
        return null;
    }
}
