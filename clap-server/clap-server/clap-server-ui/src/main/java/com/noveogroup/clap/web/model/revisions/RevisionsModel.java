package com.noveogroup.clap.web.model.revisions;


import com.google.common.collect.Lists;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.revision.Revision;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class RevisionsModel implements Serializable {

    private RevisionsListDataModel revisionsListDataModel;
    private Revision selectedRevision;
    private List<SelectItem> revisionTypes;

    private final List<CrashMessage> selectedRevCrashes = Lists.newArrayList();
    private final List<ScreenshotMessage> selectedRevScreenshots = Lists.newArrayList();
    private final List<LogsBunchMessage> selectedRevLogs = Lists.newArrayList();
    private final List<RevisionPackageModel> variants = Lists.newArrayList();

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

    public List<RevisionPackageModel> getVariants() {
        return variants;
    }
}
