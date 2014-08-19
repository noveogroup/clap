package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.CrashMessage;

public class CrashMessageRequest extends BaseMessageRequest {

    private CrashMessage message;

    public CrashMessage getMessage() {
        return message;
    }

    public void setMessage(final CrashMessage message) {
        this.message = message;
    }

}
