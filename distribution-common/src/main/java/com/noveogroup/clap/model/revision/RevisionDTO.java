package com.noveogroup.clap.model.revision;

import com.noveogroup.clap.model.BaseDTO;
import com.noveogroup.clap.model.message.MessageDTO;

import java.util.List;

/**
 * @author Mikhail Demidov
 */
public class RevisionDTO extends BaseDTO {

    private Long timestamp;

    private RevisionType revisionType;

    private List<MessageDTO> messages;

    private Long projectId;

    private String mainPackageUrl;

    private String specialPackageUrl;


    public RevisionDTO() {
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

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(final List<MessageDTO> messages) {
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
}
