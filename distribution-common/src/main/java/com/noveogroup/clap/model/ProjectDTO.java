package com.noveogroup.clap.model;

import com.noveogroup.clap.model.revision.RevisionDTO;

import java.util.Date;
import java.util.List;

public class ProjectDTO extends BaseDTO {


    private String name;

    private String description;

    private Date creationDate;

    private List<RevisionDTO> revisions;


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

    public List<RevisionDTO> getRevisions() {
        return revisions;
    }

    public void setRevisions(final List<RevisionDTO> revisions) {
        this.revisions = revisions;
    }
}
