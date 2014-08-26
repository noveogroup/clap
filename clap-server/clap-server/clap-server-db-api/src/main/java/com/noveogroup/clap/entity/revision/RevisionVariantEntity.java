package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.user.UserEntity;

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
 * @author Andrey Sokolov
 */
@Entity(name = "RevisionVariantEntity")
@Table(name = "revision_variants")
@NamedQueries({
        @NamedQuery(name = "getRevisionVariantByHash",
                query = "SELECT rev FROM RevisionVariantEntity rev WHERE rev.fullHash = :hash"),
        @NamedQuery(name = "getRevisionVariantByMessageId",
                query = "SELECT rev FROM RevisionVariantEntity rev INNER JOIN rev.messages m WHERE m.id = :id")
})
public class RevisionVariantEntity extends BaseEntity {

    @ManyToOne(optional = false)
    private RevisionEntity revision;
    @Column(name = "fullHash", nullable = false)
    private String fullHash;
    @Column(name = "packageFileUrl", nullable = false)
    private String packageFileUrl;
    @ManyToOne(optional = true)
    private UserEntity packageUploadedBy;
    @Column(name = "package", nullable = true)
    private String packageVariant;
    @Column(name = "apk_structure", length = COLUMN_LENGTH)
    private String apkStructureJSON;
    @Column(name = "random", nullable = false)
    private String random;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "revisionVariant", orphanRemoval = true)
    private List<BaseMessageEntity> messages;

    public String getFullHash() {
        return fullHash;
    }

    public void setFullHash(final String fullHash) {
        this.fullHash = fullHash;
    }

    public String getPackageFileUrl() {
        return packageFileUrl;
    }

    public void setPackageFileUrl(final String packageFileUrl) {
        this.packageFileUrl = packageFileUrl;
    }

    public UserEntity getPackageUploadedBy() {
        return packageUploadedBy;
    }

    public void setPackageUploadedBy(final UserEntity packageUploadedBy) {
        this.packageUploadedBy = packageUploadedBy;
    }

    public String getPackageVariant() {
        return packageVariant;
    }

    public void setPackageVariant(final String packageVariant) {
        this.packageVariant = packageVariant;
    }

    public RevisionEntity getRevision() {
        return revision;
    }

    public void setRevision(final RevisionEntity revision) {
        this.revision = revision;
    }

    public String getApkStructureJSON() {
        return apkStructureJSON;
    }

    public void setApkStructureJSON(final String apkStructureJSON) {
        this.apkStructureJSON = apkStructureJSON;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(final String random) {
        this.random = random;
    }

    public List<BaseMessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(final List<BaseMessageEntity> messageEntities) {
        this.messages = messageEntities;
    }
}
