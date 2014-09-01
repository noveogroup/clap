package com.noveogroup.clap.model.request.revision;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

/**
 * upload apk request
 */
public class CreateRevisionVariantRequest {

    @FormParam("projectExternalId")
    @PartType("text/plain")
    private String projectExternalId;

    @FormParam("revisionHash")
    @PartType("text/plain")
    private String revisionHash;

    @FormParam("variantHash")
    @PartType("text/plain")
    private String variantHash;

    @FormParam("packageStream")
    @PartType("application/octet-stream")
    private InputStream packageStream;

    @FormParam("variantName")
    @PartType("text/plain")
    private String variantName;

    @FormParam("random")
    @PartType("text/plain")
    private String random;

    @FormParam("token")
    @PartType("text/plain")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getProjectExternalId() {
        return projectExternalId;
    }

    public void setProjectExternalId(final String projectExternalId) {
        this.projectExternalId = projectExternalId;
    }

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }

    public InputStream getPackageStream() {
        return packageStream;
    }

    public void setPackageStream(final InputStream packageStream) {
        this.packageStream = packageStream;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(final String variantName) {
        this.variantName = variantName;
    }

    public String getVariantHash() {
        return variantHash;
    }

    public void setVariantHash(final String variantHash) {
        this.variantHash = variantHash;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(final String random) {
        this.random = random;
    }
}
