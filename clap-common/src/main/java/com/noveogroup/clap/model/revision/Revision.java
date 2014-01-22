package com.noveogroup.clap.model.revision;

import com.noveogroup.clap.model.BaseModel;
import com.noveogroup.clap.model.message.Message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@XmlRootElement
public class Revision extends BaseModel {

    private Long timestamp;

    private RevisionType revisionType;

    private List<Message> messages;

    private Long projectId;

    private String hash;

    private String mainPackageUrl;

    private String specialPackageUrl;

    private String uploadedBy;


    public Revision() {
    }

    @XmlElement
    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(final RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    @XmlElement
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(final List<Message> messages) {
        this.messages = messages;
    }

    @XmlElement
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(final Long projectId) {
        this.projectId = projectId;
    }

    @XmlElement
    public String getMainPackageUrl() {
        return mainPackageUrl;
    }

    public void setMainPackageUrl(final String mainPackageUrl) {
        this.mainPackageUrl = mainPackageUrl;
    }

    @XmlElement
    public String getSpecialPackageUrl() {
        return specialPackageUrl;
    }

    public void setSpecialPackageUrl(final String specialPackageUrl) {
        this.specialPackageUrl = specialPackageUrl;
    }

    @XmlElement
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    @XmlElement
    public String getHash() {
        return hash;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Revision{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", revisionType=").append(revisionType);
        sb.append(", messages=").append(messages);
        sb.append(", projectId=").append(projectId);
        sb.append(", hash='").append(hash).append('\'');
        sb.append(", mainPackageUrl='").append(mainPackageUrl).append('\'');
        sb.append(", specialPackageUrl='").append(specialPackageUrl).append('\'');
        sb.append(", uploadedBy=").append(uploadedBy);
        sb.append('}');
        return sb.toString();
    }
}
