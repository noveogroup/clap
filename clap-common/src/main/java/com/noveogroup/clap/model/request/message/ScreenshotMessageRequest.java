package com.noveogroup.clap.model.request.message;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessageRequest {
    @FormParam("screenshotFile")
    @PartType("application/octet-stream")
    private InputStream screenshotFileStream;
    @FormParam("token")
    @PartType("text/plain")
    private String token;
    @FormParam("revisionHash")
    @PartType("text/plain")
    private String revisionHash;

    public InputStream getScreenshotFileStream() {
        return screenshotFileStream;
    }

    public void setScreenshotFileStream(final InputStream screenshotFileStream) {
        this.screenshotFileStream = screenshotFileStream;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }
}
