package com.noveogroup.clap.entity;

import com.noveogroup.clap.entity.revision.Revision;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    private static final long serialVersionUID = 8306757495649843962L;

    private String name;

    private String description;

    private Long creationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<Revision> revisions;

    /**
     * Constructor
     */
    public Project() {
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

    /**
     * Sets new creationDate.
     *
     * @param creationDate New value of creationDate.
     */
    public void setCreationDate(final Long creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets creationDate.
     *
     * @return Value of creationDate.
     */
    public Long getCreationDate() {
        return creationDate;
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
     * @param revisions New value of revisions.
     */
    public void setRevisions(final List<Revision> revisions) {
        this.revisions = revisions;
    }

    /**
     * Gets revisions.
     *
     * @return Value of revisions.
     */
    public List<Revision> getRevisions() {
        return revisions;
    }
}
