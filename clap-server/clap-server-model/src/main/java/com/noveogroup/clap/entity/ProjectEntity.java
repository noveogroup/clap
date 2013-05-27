package com.noveogroup.clap.entity;

import com.noveogroup.clap.entity.revision.RevisionEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
@NamedQuery(name = "getProjectByExternalId", query = "SELECT p FROM ProjectEntity p WHERE p.externalId = :externalId")
public class ProjectEntity extends BaseEntity {

    private static final long serialVersionUID = 8306757495649843962L;

    private String name;

    @Column(unique = true,nullable = false)
    private String externalId;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<RevisionEntity> revisions;

    public ProjectEntity() {
        revisions = new ArrayList<RevisionEntity>();
    }

    public void setDescription(final String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRevisions(final List<RevisionEntity> revisionEntities) {
        this.revisions = revisionEntities;
    }

    public List<RevisionEntity> getRevisions() {
        return revisions;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProjectEntity{");
        sb.append("name='").append(name).append('\'');
        sb.append(", externalId='").append(externalId).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", revisions=").append(revisions);
        sb.append('}');
        return sb.toString();
    }
}
