package com.noveogroup.clap.model;

import com.noveogroup.clap.model.revision.Revision;

import java.util.Date;
import java.util.List;

public class Project extends BaseModel {


    private String name;

    //groupId:artifactId
    private String externalId;

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

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(final List<Revision> revisions) {
        this.revisions = revisions;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Project{");
        sb.append("name='").append(name).append('\'');
        sb.append(", externalId='").append(externalId).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", revisions=").append(revisions);
        sb.append('}');
        return sb.toString();
    }
}
