package com.noveogroup.clap.model.message;

import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessage extends BaseMessage implements Serializable {

    private String screenshotUrl;

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(final String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    @Override
    public MessageType type() {
        return MessageType.SCREENSHOT;
    }
}
