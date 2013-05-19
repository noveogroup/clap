package com.noveogroup.clap.model.request.revision;

import com.noveogroup.clap.model.revision.ApplicationType;

/**
 * @author Andrey Sokolov
 */
public class GetApplicationRequest extends RevisionRequest {
    private ApplicationType applicationType;

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }
}
