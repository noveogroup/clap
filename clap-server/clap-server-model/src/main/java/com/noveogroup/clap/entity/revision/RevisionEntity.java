package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.model.revision.RevisionType;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Blob;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Entity
@Table(name = "revisions")
@NamedQuery(name = "getRevisionByHash", query = "SELECT rev FROM RevisionEntity rev WHERE rev.hash = :hash")
public class RevisionEntity extends BaseEntity {

    private static final int COLUMN_LENGTH = 16777215;

    private Long timestamp;

    private RevisionType revisionType;

    @Column(unique = true)
    private String hash;

    @Column(name = "main_package")
    @Lob
    private Blob mainPackage;

    @Column(name = "special_package")
    @Lob
    private Blob specialPackage;

    private boolean mainPackageLoaded;

    private boolean specialPackageLoaded;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MessageEntity> messages;

    @Column(length = COLUMN_LENGTH)
    private String apkStructureJSON;

    @ManyToOne(optional = false)
    private ProjectEntity project;

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

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(final List<MessageEntity> messageEntities) {
        this.messages = messageEntities;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(final ProjectEntity projectEntity) {
        this.project = projectEntity;
    }

    public boolean isMainPackageLoaded() {
        return mainPackageLoaded;
    }

    public void setMainPackageLoaded(final boolean mainPackageLoaded) {
        this.mainPackageLoaded = mainPackageLoaded;
    }

    public boolean isSpecialPackageLoaded() {
        return specialPackageLoaded;
    }

    public void setSpecialPackageLoaded(final boolean specialPackageLoaded) {
        this.specialPackageLoaded = specialPackageLoaded;
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

    public Blob getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(final Blob mainPackage) {
        this.mainPackage = mainPackage;
    }

    public Blob getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(final Blob specialPackage) {
        this.specialPackage = specialPackage;
    }

    public String getApkStructureJSON() {
        return apkStructureJSON;
    }

    public void setApkStructureJSON(final String apkStructureJSON) {
        this.apkStructureJSON = apkStructureJSON;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("revisionType", revisionType)
                .append("hash", hash)
                .append("mainPackageLoaded", mainPackageLoaded)
                .append("specialPackageLoaded", specialPackageLoaded)
                .append("messages", messages)
                .append("project", project)
                .append("apkStructureJSON", apkStructureJSON)
                .toString();
    }
}
