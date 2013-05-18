package com.noveogroup.clap.model;

import com.noveogroup.clap.model.revision.Revision;

import java.util.Date;
import java.util.List;

public class Project extends BaseModel {


    private String name;

    private String description;

    private Date creationDate;

    private List<Revision> revisions;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(final List<Revision> revisions) {
        this.revisions = revisions;
    }
}
