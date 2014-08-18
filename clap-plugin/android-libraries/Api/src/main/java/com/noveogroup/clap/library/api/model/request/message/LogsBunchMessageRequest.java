package com.noveogroup.clap.library.api.model.request.message;

import com.noveogroup.clap.library.api.model.message.LogsBunchMessage;

public class LogsBunchMessageRequest extends BaseMessageRequest {
    private LogsBunchMessage message;

    public LogsBunchMessage getMessage() {
        return message;
    }

    public void setMessage(final LogsBunchMessage message) {
        this.message = message;
    }
}
