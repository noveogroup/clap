package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;
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

    @Column(name = "package")
    @Lob
    private byte[] build;

    @OneToMany
    private List<Message> messages;


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
}
