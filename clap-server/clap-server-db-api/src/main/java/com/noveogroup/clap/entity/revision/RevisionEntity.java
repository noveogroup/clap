package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.user.UserEntity;
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

    @Column(name = "main_package", nullable = true)
    private String mainPackageFileUrl;

    @Column(name = "special_package", nullable = true)
    private String specialPackageFileUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "revision", orphanRemoval = true)
    private List<BaseMessageEntity> messages;

    @Column(name = "apk_structure", length = COLUMN_LENGTH)
    private String apkStructureJSON;

    @ManyToOne(optional = false)
    private ProjectEntity project;

    @ManyToOne(optional = true)
    private UserEntity mainPackageUploadedBy;

    @ManyToOne(optional = true)
    private UserEntity specialPackageUploadedBy;

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

    public List<BaseMessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(final List<BaseMessageEntity> messageEntities) {
        this.messages = messageEntities;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(final ProjectEntity projectEntity) {
        this.project = projectEntity;
    }

    public boolean isMainPackageLoaded() {
        return mainPackageFileUrl != null;
    }

    public boolean isSpecialPackageLoaded() {
        return specialPackageFileUrl != null;
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

    public String getApkStructureJSON() {
        return apkStructureJSON;
    }

    public void setApkStructureJSON(final String apkStructureJSON) {
        this.apkStructureJSON = apkStructureJSON;
    }

    public UserEntity getMainPackageUploadedBy() {
        return mainPackageUploadedBy;
    }

    public void setMainPackageUploadedBy(final UserEntity mainPackageUploadedBy) {
        this.mainPackageUploadedBy = mainPackageUploadedBy;
    }

    public UserEntity getSpecialPackageUploadedBy() {
        return specialPackageUploadedBy;
    }

    public void setSpecialPackageUploadedBy(final UserEntity specialPackageUploadedBy) {
        this.specialPackageUploadedBy = specialPackageUploadedBy;
    }

    public String getMainPackageFileUrl() {
        return mainPackageFileUrl;
    }

    public void setMainPackageFileUrl(final String mainPackageFileUrl) {
        this.mainPackageFileUrl = mainPackageFileUrl;
    }

    public String getSpecialPackageFileUrl() {
        return specialPackageFileUrl;
    }

    public void setSpecialPackageFileUrl(final String specialPackageFileUrl) {
        this.specialPackageFileUrl = specialPackageFileUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("revisionType", revisionType)
                .append("hash", hash)
                .append("mainPackage", mainPackageFileUrl)
                .append("specialPackage", specialPackageFileUrl)
                .append("messages", messages)
                .append("apkStructureJSON", apkStructureJSON)
                .append("project", project)
                .append("mainPackageUploadedBy", mainPackageUploadedBy)
                .append("specialPackageUploadedBy", specialPackageUploadedBy)
                .toString();
    }
}
