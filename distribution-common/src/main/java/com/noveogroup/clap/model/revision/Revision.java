package com.noveogroup.clap.model.revision;

import com.noveogroup.clap.model.BaseModel;
import com.noveogroup.clap.model.message.Message;

import java.util.List;

/**
 * @author Mikhail Demidov
 */
public class Revision extends BaseModel {

    private Long timestamp;

    private RevisionType revisionType;

    private List<Message> messages;

    private Long projectId;

    private String mainPackageUrl;

    private String specialPackageUrl;


    public Revision() {
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(final Long projectId) {
        this.projectId = projectId;
    }

    public String getMainPackageUrl() {
        return mainPackageUrl;
    }

    public void setMainPackageUrl(final String mainPackageUrl) {
        this.mainPackageUrl = mainPackageUrl;
    }

    public String getSpecialPackageUrl() {
        return specialPackageUrl;
    }

    public void setSpecialPackageUrl(final String specialPackageUrl) {
        this.specialPackageUrl = specialPackageUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
