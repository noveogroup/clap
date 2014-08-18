package com.noveogroup.clap.model.revision;

import com.noveogroup.clap.model.BaseModel;

/**
 * @author Andrey Sokolov
 */
public class RevisionVariant extends BaseModel {

    private String fullHash;
    private String packageUrl;
    private String packageUploadedBy;
    private String packageVariant;

    public String getFullHash() {
        return fullHash;
    }

    public void setFullHash(final String fullHash) {
        this.fullHash = fullHash;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(final String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getPackageUploadedBy() {
        return packageUploadedBy;
    }

    public void setPackageUploadedBy(final String packageUploadedBy) {
        this.packageUploadedBy = packageUploadedBy;
    }

    public String getPackageVariant() {
        return packageVariant;
    }

    public void setPackageVariant(final String packageVariant) {
        this.packageVariant = packageVariant;
    }
}
