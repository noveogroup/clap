package com.noveogroup.clap.web.model.revisions;

import com.noveogroup.clap.model.revision.RevisionWithApkStructure;

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
    private RevisionWithApkStructure selectedRevision;
    private List<SelectItem> revisionTypes;
    public RevisionWithApkStructure getSelectedRevision() {
        return selectedRevision;
    }

    public void setSelectedRevision(final RevisionWithApkStructure selectedRevision) {
        this.selectedRevision = selectedRevision;
    }

    public List<SelectItem> getRevisionTypes() {
        return revisionTypes;
    }

    public void setRevisionTypes(final List<SelectItem> revisionTypes) {
        this.revisionTypes = revisionTypes;
    }
}
