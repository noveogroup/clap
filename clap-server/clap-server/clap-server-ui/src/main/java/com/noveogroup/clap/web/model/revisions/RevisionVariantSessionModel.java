package com.noveogroup.clap.web.model.revisions;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.InfoMessage;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import org.primefaces.model.TreeNode;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class RevisionVariantSessionModel implements Serializable{
    private RevisionVariantWithApkStructure selectedRevisionVariant;
    private CrashMessage selectedCrashMessage;
    private LogsBunchMessage selectedLogsMessage;

    private TreeNode selectedVariantApkStructure;

    private final List<CrashMessage> selectedRevCrashes = Lists.newArrayList();
    private final List<ScreenshotMessage> selectedRevScreenshots = Lists.newArrayList();
    private final List<LogsBunchMessage> selectedRevLogs = Lists.newArrayList();
    private final List<InfoMessage> selectedRevInfos = Lists.newArrayList();

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

    public CrashMessage getSelectedCrashMessage() {
        return selectedCrashMessage;
    }

    public void setSelectedCrashMessage(final CrashMessage selectedCrashMessage) {
        this.selectedCrashMessage = selectedCrashMessage;
    }

    public LogsBunchMessage getSelectedLogsMessage() {
        return selectedLogsMessage;
    }

    public void setSelectedLogsMessage(final LogsBunchMessage selectedLogsMessage) {
        this.selectedLogsMessage = selectedLogsMessage;
    }

    public List<InfoMessage> getSelectedRevInfos() {
        return selectedRevInfos;
    }
}
