package com.noveogroup.clap.web.controller;

import org.primefaces.event.FileUploadEvent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RevisionsController {

    public String saveNewRevision(){
        //TODO
        return null;
    }

    public void handleNewRevisionCleanApkUpload(FileUploadEvent event){
        //TODO

    }

    public void handleNewRevisionHackedApkUpload(FileUploadEvent event){
        //TODO

    }
}
