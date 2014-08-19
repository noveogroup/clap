package com.noveogroup.clap.model.revision;

import com.noveogroup.clap.model.BaseModel;
import com.noveogroup.clap.model.message.BaseMessage;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Mikhail Demidov
 */
public class Revision extends BaseModel {

    private Long timestamp;

    private RevisionType revisionType;

    private List<BaseMessage> messages;

    private Long projectId;

    private String hash;

    private List<RevisionVariant> variants;

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(final RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    public List<BaseMessage> getMessages() {
        return messages;
    }

    public void setMessages(final List<BaseMessage> messages) {
        this.messages = messages;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(final Long projectId) {
        this.projectId = projectId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }


    public List<RevisionVariant> getVariants() {
        return variants;
    }

    public void setVariants(final List<RevisionVariant> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("revisionType", revisionType)
                .append("messages", messages)
                .append("projectId", projectId)
                .append("hash", hash)
                .toString();
    }
}
