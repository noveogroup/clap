package com.noveogroup.clap.model.request.revision;

import com.noveogroup.clap.model.request.BaseRequest;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

/**
 * upload apk request
 */
public class CreateOrUpdateRevisionRequest extends BaseRequest {

    @FormParam("projectExternalId")
    @PartType("text/plain")
    private String projectExternalId;

    @FormParam("revisionHash")
    @PartType("text/plain")
    private String revisionHash;

    @FormParam("mainPackage")
    @PartType("application/octet-stream")
    private InputStream mainPackage;

    @FormParam("specialPackage")
    @PartType("application/octet-stream")
    private InputStream specialPackage;

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

    public InputStream getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(final InputStream mainPackage) {
        this.mainPackage = mainPackage;
    }

    public InputStream getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(final InputStream specialPackage) {
        this.specialPackage = specialPackage;
    }

}
