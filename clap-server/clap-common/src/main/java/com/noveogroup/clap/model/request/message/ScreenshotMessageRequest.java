package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.message.ScreenshotMessage;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;
import java.util.Map;

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

    @FormParam("variantHash")
    @PartType("text/plain")
    private String variantHash;

    //TODO check if it works
    @FormParam("message")
    @PartType("application/json")
    private ScreenshotMessage message;


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

    public String getVariantHash() {
        return variantHash;
    }

    public void setVariantHash(final String variantHash) {
        this.variantHash = variantHash;
    }

    public ScreenshotMessage getMessage() {
        return message;
    }

    public void setMessage(final ScreenshotMessage message) {
        this.message = message;
    }
}
