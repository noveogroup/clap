package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.model.revision.RevisionType;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Entity(name = "RevisionEntity")
@Table(name = "revisions")
@NamedQueries({
        @NamedQuery(name = "getRevisionByHash", query = "SELECT rev FROM RevisionEntity rev WHERE rev.hash = :hash"),
        @NamedQuery(name = "getRevisionsByProjectAndType",
                query = "SELECT rev FROM RevisionEntity rev WHERE rev.project.id = :id and rev.revisionType = :type")
})
public class RevisionEntity extends BaseEntity {

    private static final int COLUMN_LENGTH = 16777215;

    @Column(name = "upload_time", nullable = false)
    private Long timestamp;

    @Column(name = "type", nullable = false)
    private RevisionType revisionType;

    @Column(name = "hash", unique = true, nullable = false)
    private String hash;

    @ManyToOne(optional = false)
    private ProjectEntity project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "revision", orphanRemoval = true)
    private List<RevisionVariantEntity> variants;

    /**
     * Constructor
     */
    public RevisionEntity() {
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(final RevisionType revisionType) {
        this.revisionType = revisionType;
    }


    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(final ProjectEntity projectEntity) {
        this.project = projectEntity;
    }


    public String getHash() {
        return hash;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }



    public List<RevisionVariantEntity> getVariants() {
        return variants;
    }

    public void setVariants(final List<RevisionVariantEntity> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("revisionType", revisionType)
                .append("hash", hash)
                .append("project", project)
                .toString();
    }
}
