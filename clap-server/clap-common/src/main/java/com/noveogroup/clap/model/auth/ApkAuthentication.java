package com.noveogroup.clap.model.auth;

import javax.validation.constraints.NotNull;

/**
 * @author Andrey Sokolov
 */
public class ApkAuthentication {
    @NotNull
    private String projectId;
    @NotNull
    private String revisionHash;
    @NotNull
    private String variantHash;
    @NotNull
    private String random;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
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

    public String getRandom() {
        return random;
    }

    public void setRandom(final String random) {
        this.random = random;
    }
}
