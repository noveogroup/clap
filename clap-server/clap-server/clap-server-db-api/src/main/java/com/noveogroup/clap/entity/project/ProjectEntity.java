package com.noveogroup.clap.entity.project;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "ProjectEntity")
@Table(name = "projects")
@NamedQuery(name = "getProjectByExternalId", query = "SELECT p FROM ProjectEntity p WHERE p.externalId = :externalId")
public class ProjectEntity extends BaseEntity {

    private static final long serialVersionUID = 8306757495649843962L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(name = "description", nullable = true)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project",orphanRemoval = true)
    private List<RevisionEntity> revisions;

    @Column(name = "icon")
    private String iconFilePath;

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

    public void setExternalId(final String externalId) {
        this.externalId = externalId;
    }

    public String getIconFilePath() {
        return iconFilePath;
    }

    public void setIconFilePath(final String iconFilePath) {
        this.iconFilePath = iconFilePath;
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
