package com.noveogroup.clap.web.model.revisions;


import com.google.common.collect.Lists;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import org.primefaces.model.TreeNode;

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
    private RevisionVariantWithApkStructure selectedRevisionVariant;
    private List<SelectItem> revisionTypes;

    private TreeNode selectedVariantApkStructure;

    private final List<CrashMessage> selectedRevCrashes = Lists.newArrayList();
    private final List<ScreenshotMessage> selectedRevScreenshots = Lists.newArrayList();
    private final List<LogsBunchMessage> selectedRevLogs = Lists.newArrayList();

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

    public List<CrashMessage> getSelectedRevCrashes() {
        return selectedRevCrashes;
    }

    public List<ScreenshotMessage> getSelectedRevScreenshots() {
        return selectedRevScreenshots;
    }

    public List<LogsBunchMessage> getSelectedRevLogs() {
        return selectedRevLogs;
    }

    public RevisionVariantWithApkStructure getSelectedRevisionVariant() {
        return selectedRevisionVariant;
    }

    public void setSelectedRevisionVariant(final RevisionVariantWithApkStructure selectedRevisionVariant) {
        this.selectedRevisionVariant = selectedRevisionVariant;
    }

    public TreeNode getSelectedVariantApkStructure() {
        return selectedVariantApkStructure;
    }

    public void setSelectedVariantApkStructure(final TreeNode selectedVariantApkStructure) {
        this.selectedVariantApkStructure = selectedVariantApkStructure;
    }
}
