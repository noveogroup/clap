package com.noveogroup.clap.web.model.revisions;


import com.noveogroup.clap.model.revision.Revision;
import org.primefaces.model.TreeNode;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RevisionsModel implements Serializable {

    private RevisionsListDataModel revisionsListDataModel;
    private Revision selectedRevision;
    private TreeNode selectedRevisionApkStructure;

    private RevisionPackageModel cleanPackageModel = new RevisionPackageModel();
    private RevisionPackageModel hackedPackageModel = new RevisionPackageModel();


    public void reset() {
        cleanPackageModel = new RevisionPackageModel();
        hackedPackageModel = new RevisionPackageModel();
        selectedRevisionApkStructure = null;
    }


    public RevisionsListDataModel getRevisionsListDataModel() {
        return revisionsListDataModel;
    }

    public void setRevisionsListDataModel(final RevisionsListDataModel revisionsListDataModel) {
        this.revisionsListDataModel = revisionsListDataModel;
    }

    public Revision getSelectedRevision() {
        return selectedRevision;
    }

    public void setSelectedRevision(final Revision selectedRevision) {
        this.selectedRevision = selectedRevision;
    }

    public TreeNode getSelectedRevisionApkStructure() {
        return selectedRevisionApkStructure;
    }

    public void setSelectedRevisionApkStructure(final TreeNode selectedRevisionApkStructure) {
        this.selectedRevisionApkStructure = selectedRevisionApkStructure;
    }

    public RevisionPackageModel getCleanPackageModel() {
        return cleanPackageModel;
    }

    public RevisionPackageModel getHackedPackageModel() {
        return hackedPackageModel;
    }

}
