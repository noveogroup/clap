package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.ScreenshotMessage;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessageRequest extends BaseMessageRequest {
    private ScreenshotMessage message;

    public ScreenshotMessage getMessage() {
        return message;
    }

    public void setMessage(final ScreenshotMessage message) {
        this.message = message;
    }
}
