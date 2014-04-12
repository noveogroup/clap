package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.ScreenshotMessage;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessageRequest extends BaseMessageRequest {
    @FormParam("messageModel")
    @PartType("application/json")
    private ScreenshotMessage message;

    @FormParam("screenshotFile")
    @PartType("application/octet-stream")
    private InputStream screenshotFileStream;

    public InputStream getScreenshotFileStream() {
        return screenshotFileStream;
    }

    public void setScreenshotFileStream(final InputStream screenshotFileStream) {
        this.screenshotFileStream = screenshotFileStream;
    }

    public ScreenshotMessage getMessage() {
        return message;
    }

    public void setMessage(final ScreenshotMessage message) {
        this.message = message;
    }
}
