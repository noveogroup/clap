package com.noveogroup.clap.web.model.revisions;


import com.noveogroup.clap.model.revision.RevisionWithApkStructure;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class RevisionsModel implements Serializable {

    private RevisionsListDataModel revisionsListDataModel;
    private RevisionWithApkStructure selectedRevision;
    private List<SelectItem> revisionTypes;

    public RevisionsListDataModel getRevisionsListDataModel() {
        return revisionsListDataModel;
    }

    public void setRevisionsListDataModel(final RevisionsListDataModel revisionsListDataModel) {
        this.revisionsListDataModel = revisionsListDataModel;
    }

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
