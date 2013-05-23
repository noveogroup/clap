package com.noveogroup.clap.web.model;


import com.noveogroup.clap.model.revision.Revision;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RevisionsModel implements Serializable{

    private RevisionsListDataModel revisionsListDataModel;
    private Revision selectedRevision;

    private StreamedContent cleanApkQRCode;
    private StreamedContent hackedApkQRCode;

    private UploadedFile uploadCleanApk;
    private UploadedFile uploadHackedApk;

    public void reset(){
        uploadCleanApk = null;
        uploadHackedApk = null;
    }

    public UploadedFile getUploadCleanApk() {
        return uploadCleanApk;
    }

    public void setUploadCleanApk(final UploadedFile uploadCleanApk) {
        this.uploadCleanApk = uploadCleanApk;
    }

    public UploadedFile getUploadHackedApk() {
        return uploadHackedApk;
    }

    public void setUploadHackedApk(final UploadedFile uploadHackedApk) {
        this.uploadHackedApk = uploadHackedApk;
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

    public StreamedContent getCleanApkQRCode() {
        return cleanApkQRCode;
    }

    public void setCleanApkQRCode(final StreamedContent cleanApkQRCode) {
        this.cleanApkQRCode = cleanApkQRCode;
    }

    public StreamedContent getHackedApkQRCode() {
        return hackedApkQRCode;
    }

    public void setHackedApkQRCode(final StreamedContent hackedApkQRCode) {
        this.hackedApkQRCode = hackedApkQRCode;
    }
}
