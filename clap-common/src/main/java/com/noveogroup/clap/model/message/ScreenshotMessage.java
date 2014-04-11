package com.noveogroup.clap.model.message;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessage extends BaseMessage {

    private byte[] screenshot;

    public byte[] getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(final byte[] screenshot) {
        this.screenshot = screenshot;
    }

    @Override
    public Class<? extends BaseMessage> getMessageType() {
        return ScreenshotMessage.class;
    }
}
