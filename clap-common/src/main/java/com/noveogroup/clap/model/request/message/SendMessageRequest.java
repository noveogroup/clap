package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.CrashMessage;

public class SendMessageRequest extends BaseMessageRequest {

    private CrashMessage message;

    public CrashMessage getMessage() {
        return message;
    }

    public void setMessage(final CrashMessage message) {
        this.message = message;
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
