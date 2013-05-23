package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.Message;

public class SendMessageRequest {

    private Message message;

    private String revisionHash;

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendMessageRequest{");
        sb.append("message=").append(message);
        sb.append(", revisionHash=").append(revisionHash);
        sb.append('}');
        return sb.toString();
    }
}
