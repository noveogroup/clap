package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.web.model.RevisionsModel;
import org.primefaces.event.FileUploadEvent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class RevisionsController {

    @Inject
    private RevisionsModel revisionsModel;

    public String saveNewRevision(){
        //TODO
        return null;
    }
}
