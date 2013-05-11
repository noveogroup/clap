package com.noveogroup.clap.rest.model;

import com.noveogroup.clap.model.message.MessageDTO;

public class SendMessageRequest {

    private MessageDTO messageDTO;

    private Long revisionTimestamp;

    private String projectName;

    public MessageDTO getMessageDTO() {
        return messageDTO;
    }

    public void setMessageDTO(MessageDTO messageDTO) {
        this.messageDTO = messageDTO;
    }

    public Long getRevisionTimestamp() {
        return revisionTimestamp;
    }

    public void setRevisionTimestamp(Long revisionTimestamp) {
        this.revisionTimestamp = revisionTimestamp;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendMessageRequest{");
        sb.append("messageDTO=").append(messageDTO);
        sb.append(", revisionTimestamp=").append(revisionTimestamp);
        sb.append(", projectName='").append(projectName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
