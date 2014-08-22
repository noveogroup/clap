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
    private Long revisionId;
    private Long projectId;

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

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(final Long revisionId) {
        this.revisionId = revisionId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(final Long projectId) {
        this.projectId = projectId;
    }
}
