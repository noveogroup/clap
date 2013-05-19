package com.noveogroup.clap.model.request.revision;

import com.noveogroup.clap.model.revision.Revision;

import javax.validation.constraints.NotNull;

/**
 * @author Andrey Sokolov
 */
public class AddOrGetRevisionRequest {

    @NotNull
    private String projectExternalId;
    @NotNull
    private Revision revision;

    private byte[] mainPackage;
    private byte[] specialPackage;

    public String getProjectExternalId() {
        return projectExternalId;
    }

    public void setProjectExternalId(String projectExternalId) {
        this.projectExternalId = projectExternalId;
    }

    public Revision getRevision() {
        return revision;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public byte[] getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(byte[] mainPackage) {
        this.mainPackage = mainPackage;
    }

    public byte[] getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(byte[] specialPackage) {
        this.specialPackage = specialPackage;
    }
}
