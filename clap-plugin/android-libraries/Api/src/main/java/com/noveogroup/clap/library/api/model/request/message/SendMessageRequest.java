package com.noveogroup.clap.library.api.model.request.message;

import com.noveogroup.clap.library.api.model.message.CrashMessage;

public class SendMessageRequest extends BaseMessageRequest {

    private CrashMessage message;

    public CrashMessage getMessage() {
        return message;
    }

    public void setMessage(final CrashMessage message) {
        this.message = message;
    }
}
