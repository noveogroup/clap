package com.noveogroup.clap.entity;

import com.noveogroup.clap.entity.revision.RevisionEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
@NamedQuery(name="getProjectByName",query="SELECT p FROM ProjectEntity p WHERE p.name = :name")
public class ProjectEntity extends BaseEntity {

    private static final long serialVersionUID = 8306757495649843962L;

    @Column(unique = true)
    private String name;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<RevisionEntity> revisions;

    /**
     * Constructor
     */
    public ProjectEntity() {
        revisions = new ArrayList<RevisionEntity>();
    }

    /**
     * Sets new description.
     *
     * @param description New value of description.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets name.
     *
     * @return Value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets description.
     *
     * @return Value of description.
     */
    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets new name.
     *
     * @param name New value of name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets new revisions.
     *
     * @param revisionEntities New value of revisions.
     */
    public void setRevisions(final List<RevisionEntity> revisionEntities) {
        this.revisions = revisionEntities;
    }

    /**
     * Gets revisions.
     *
     * @return Value of revisions.
     */
    public List<RevisionEntity> getRevisions() {
        return revisions;
    }
}
