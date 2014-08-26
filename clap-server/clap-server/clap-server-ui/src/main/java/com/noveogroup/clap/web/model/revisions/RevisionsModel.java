package com.noveogroup.clap.web.model.revisions;



import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class RevisionsModel implements Serializable {

    private RevisionsListDataModel revisionsListDataModel;

    public RevisionsListDataModel getRevisionsListDataModel() {
        return revisionsListDataModel;
    }

    public void setRevisionsListDataModel(final RevisionsListDataModel revisionsListDataModel) {
        this.revisionsListDataModel = revisionsListDataModel;
    }



}
