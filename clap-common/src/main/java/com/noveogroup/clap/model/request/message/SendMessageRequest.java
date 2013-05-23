package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.Message;

public class SendMessageRequest {

    private Message message;

    private Long revisionTimestamp;

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }

    public Long getRevisionTimestamp() {
        return revisionTimestamp;
    }

    public void setRevisionTimestamp(final Long revisionTimestamp) {
        this.revisionTimestamp = revisionTimestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendMessageRequest{");
        sb.append("message=").append(message);
        sb.append(", revisionTimestamp=").append(revisionTimestamp);
        sb.append('}');
        return sb.toString();
    }
}
