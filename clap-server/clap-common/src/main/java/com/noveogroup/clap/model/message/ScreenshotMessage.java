package com.noveogroup.clap.model.message;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessage extends BaseMessage {

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
