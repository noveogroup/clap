package com.noveogroup.clap.model.request.revision;

/**
 * @author Andrey Sokolov
 */
public class UpdateRevisionPackagesRequest {
    private byte[] mainPackage;
    private byte[] specialPackage;
    private Long revisionId;

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(final Long revisionId) {
        this.revisionId = revisionId;
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
