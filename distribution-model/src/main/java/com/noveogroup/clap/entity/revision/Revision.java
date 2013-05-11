package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.entity.message.Message;

import javax.persistence.*;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Entity
@Table(name = "revisions")
public class Revision extends BaseEntity {


    private Long timestamp;

    private RevisionType revisionType;

    @Column(name = "main_package")
    @Lob
    private byte[] mainPackage;

    @Column(name = "special_package")
    @Lob
    private byte[] specialPackage;

    private boolean mainPackageLoaded;

    private boolean specialPackageLoaded;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> messages;


    @ManyToOne
    private Project project;

    /**
     * Constructor
     */
    public Revision() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(final RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(final List<Message> messages) {
        this.messages = messages;
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

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public boolean isMainPackageLoaded() {
        return mainPackageLoaded;
    }

    public void setMainPackageLoaded(boolean mainPackageLoaded) {
        this.mainPackageLoaded = mainPackageLoaded;
    }

    public boolean isSpecialPackageLoaded() {
        return specialPackageLoaded;
    }

    public void setSpecialPackageLoaded(boolean specialPackageLoaded) {
        this.specialPackageLoaded = specialPackageLoaded;
    }
}
