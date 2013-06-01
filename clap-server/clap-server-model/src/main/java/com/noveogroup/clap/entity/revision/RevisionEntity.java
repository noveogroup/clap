package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.message.MessageEntity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Entity
@Table(name = "revisions")
@NamedQuery(name="getRevisionByHash",query="SELECT rev FROM RevisionEntity rev WHERE rev.hash = :hash")
public class RevisionEntity extends BaseEntity {

    private Long timestamp;

    private RevisionType revisionType;

    @Column(unique = true)
    private String hash;

    @Column(name = "main_package")
    @Lob
    private byte[] mainPackage;

    @Column(name = "special_package")
    @Lob
    private byte[] specialPackage;

    private boolean mainPackageLoaded;

    private boolean specialPackageLoaded;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MessageEntity> messages;


    @ManyToOne
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

    public byte[] getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(final byte[] mainPackage) {
        this.mainPackage = mainPackage;
    }

    public byte[] getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(final byte[] specialPackage) {
        this.specialPackage = specialPackage;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RevisionEntity{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", revisionType=").append(revisionType);
        sb.append(", hash='").append(hash).append('\'');
        sb.append(", mainPackage=").append(Arrays.toString(mainPackage));
        sb.append(", specialPackage=").append(Arrays.toString(specialPackage));
        sb.append(", mainPackageLoaded=").append(mainPackageLoaded);
        sb.append(", specialPackageLoaded=").append(specialPackageLoaded);
        sb.append(", messages=").append(messages);
        sb.append(", project=").append(project);
        sb.append('}');
        return sb.toString();
    }
}
