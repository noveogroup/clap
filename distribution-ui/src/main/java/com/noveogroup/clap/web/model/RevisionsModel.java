package com.noveogroup.clap.web.model;


import com.noveogroup.clap.model.revision.RevisionDTO;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RevisionsModel implements Serializable{

    private RevisionsListDataModel revisionsListDataModel;
    private RevisionDTO selectedRevisionDTO;

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

    public void setUploadCleanApk(UploadedFile uploadCleanApk) {
        this.uploadCleanApk = uploadCleanApk;
    }

    public UploadedFile getUploadHackedApk() {
        return uploadHackedApk;
    }

    public void setUploadHackedApk(UploadedFile uploadHackedApk) {
        this.uploadHackedApk = uploadHackedApk;
    }

    public RevisionsListDataModel getRevisionsListDataModel() {
        return revisionsListDataModel;
    }

    public void setRevisionsListDataModel(RevisionsListDataModel revisionsListDataModel) {
        this.revisionsListDataModel = revisionsListDataModel;
    }

    public RevisionDTO getSelectedRevisionDTO() {
        return selectedRevisionDTO;
    }

    public void setSelectedRevisionDTO(RevisionDTO selectedRevisionDTO) {
        this.selectedRevisionDTO = selectedRevisionDTO;
    }

    public StreamedContent getCleanApkQRCode() {
        return cleanApkQRCode;
    }

    public void setCleanApkQRCode(StreamedContent cleanApkQRCode) {
        this.cleanApkQRCode = cleanApkQRCode;
    }

    public StreamedContent getHackedApkQRCode() {
        return hackedApkQRCode;
    }

    public void setHackedApkQRCode(StreamedContent hackedApkQRCode) {
        this.hackedApkQRCode = hackedApkQRCode;
    }
}
