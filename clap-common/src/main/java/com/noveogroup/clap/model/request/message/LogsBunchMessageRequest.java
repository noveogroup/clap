package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.LogsBunchMessage;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessageRequest extends BaseMessageRequest {
    private LogsBunchMessage message;

    public LogsBunchMessage getMessage() {
        return message;
    }

    public void setMessage(final LogsBunchMessage message) {
        this.message = message;
    }
}
