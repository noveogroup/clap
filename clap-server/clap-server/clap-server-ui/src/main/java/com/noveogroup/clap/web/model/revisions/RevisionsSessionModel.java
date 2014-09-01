package com.noveogroup.clap.web.model.revisions;

import com.noveogroup.clap.model.revision.Revision;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class RevisionsSessionModel implements Serializable{
    private Revision selectedRevision;
    private List<SelectItem> revisionTypes;
    public Revision getSelectedRevision() {
        return selectedRevision;
    }

    public void setSelectedRevision(final Revision selectedRevision) {
        this.selectedRevision = selectedRevision;
    }

    public List<SelectItem> getRevisionTypes() {
        return revisionTypes;
    }

    public void setRevisionTypes(final List<SelectItem> revisionTypes) {
        this.revisionTypes = revisionTypes;
    }
}
