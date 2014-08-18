package com.noveogroup.clap.web.model.revisions;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;


/**
 * @author Andrey Sokolov
 */
public class RevisionPackageModel {
    private StreamedContent apkQRCode;
    private UploadedFile uploadApk;
    private TreeNode selectedRevisionApkStructure;

    public StreamedContent getApkQRCode() {
        return apkQRCode;
    }

    public void setApkQRCode(final StreamedContent apkQRCode) {
        this.apkQRCode = apkQRCode;
    }

    public UploadedFile getUploadApk() {
        return uploadApk;
    }

    public void setUploadApk(final UploadedFile uploadApk) {
        this.uploadApk = uploadApk;
    }


    public TreeNode getSelectedRevisionApkStructure() {
        return selectedRevisionApkStructure;
    }

    public void setSelectedRevisionApkStructure(final TreeNode selectedRevisionApkStructure) {
        this.selectedRevisionApkStructure = selectedRevisionApkStructure;
    }

}
