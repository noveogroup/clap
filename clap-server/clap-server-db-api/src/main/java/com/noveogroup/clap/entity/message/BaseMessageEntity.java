package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Andrey Sokolov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType= DiscriminatorType.STRING,length=20)
public abstract class BaseMessageEntity extends BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date timestamp;
    @ManyToOne(optional = false)
    private RevisionEntity revision;
    @ManyToOne(optional = false)
    private UserEntity uploadedBy;


    public abstract Class<? extends BaseMessageEntity> getEntityType();

    public UserEntity getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final UserEntity uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public void setRevision(final RevisionEntity revision) {
        this.revision = revision;
    }

    public RevisionEntity getRevision() {
        return revision;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
}
