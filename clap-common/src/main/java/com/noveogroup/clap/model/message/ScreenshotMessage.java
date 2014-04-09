package com.noveogroup.clap.model.message;

import java.io.InputStream;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessage extends BaseMessage {

    private InputStream screenshot;

    public InputStream getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(final InputStream screenshot) {
        this.screenshot = screenshot;
    }
}
