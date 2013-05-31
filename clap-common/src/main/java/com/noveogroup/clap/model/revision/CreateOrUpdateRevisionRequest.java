package com.noveogroup.clap.model.revision;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

/**
 * Created with IntelliJ IDEA.
 * User: mdemidov
 * Date: 5/31/13
 * Time: 6:42 PM
 *
 * @since 5/31/13 6:42 PM
 */
public class CreateOrUpdateRevisionRequest {

    @FormParam("projectExternalId")
    @PartType("multipart/form-data")
    private String projectExternalId;

    @FormParam("revisionHash")
    @PartType("multipart/form-data")
    private String revisionHash;

    @FormParam("mainPackage")
    @PartType("application/octet-stream")
    private byte[] mainPackage;

    @FormParam("specialPackage")
    @PartType("application/octet-stream")
    private byte[] specialPackage;

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

    public byte[] getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(final byte[] mainPackage) {
        this.mainPackage = mainPackage;
    }

    public byte[] getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(final byte[] specialPackage) {
        this.specialPackage = specialPackage;
    }
}
