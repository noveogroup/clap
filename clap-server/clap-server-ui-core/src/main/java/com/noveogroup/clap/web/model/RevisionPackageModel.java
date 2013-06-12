package com.noveogroup.clap.web.model;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


/**
 * @author Andrey Sokolov
 */
public class RevisionPackageModel {
    private StreamedContent apkQRCode;
    private UploadedFile uploadApk;

    public StreamedContent getApkQRCode() {
        return apkQRCode;
    }

    public void setApkQRCode(StreamedContent apkQRCode) {
        this.apkQRCode = apkQRCode;
    }

    public UploadedFile getUploadApk() {
        return uploadApk;
    }

    public void setUploadApk(UploadedFile uploadApk) {
        this.uploadApk = uploadApk;
    }
}
