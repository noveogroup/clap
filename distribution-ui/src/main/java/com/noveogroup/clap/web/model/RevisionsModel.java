package com.noveogroup.clap.web.model;

import org.primefaces.model.UploadedFile;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RevisionsModel implements Serializable{

    private UploadedFile newRevisionCleanApk;
    private UploadedFile newRevisionHackedApk;

    public void reset(){
        newRevisionCleanApk = null;
        newRevisionHackedApk = null;
    }

    public UploadedFile getNewRevisionCleanApk() {
        return newRevisionCleanApk;
    }

    public void setNewRevisionCleanApk(UploadedFile newRevisionCleanApk) {
        this.newRevisionCleanApk = newRevisionCleanApk;
    }

    public UploadedFile getNewRevisionHackedApk() {
        return newRevisionHackedApk;
    }

    public void setNewRevisionHackedApk(UploadedFile newRevisionHackedApk) {
        this.newRevisionHackedApk = newRevisionHackedApk;
    }
}
