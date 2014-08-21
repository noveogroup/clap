package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.InfoMessage;

/**
 * @author Andrey Sokolov
 */
public class InfoMessageRequest extends BaseMessageRequest {
    private InfoMessage message;

    public InfoMessage getMessage() {
        return message;
    }

    public void setMessage(final InfoMessage message) {
        this.message = message;
    }
}
