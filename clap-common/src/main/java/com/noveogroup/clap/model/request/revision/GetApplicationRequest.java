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

    public void setApplicationType(final ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GetApplicationRequest{");
        sb.append("applicationType=").append(applicationType);
        sb.append('}');
        return sb.toString();
    }
}
