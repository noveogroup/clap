package com.noveogroup.clap.model.request.revision;

import com.noveogroup.clap.model.revision.Revision;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Andrey Sokolov
 */
public class AddOrGetRevisionRequest extends BaseRevisionPackagesRequest {

    @NotNull
    private String projectExternalId;
    @NotNull
    private Revision revision;

    public String getProjectExternalId() {
        return projectExternalId;
    }

    public void setProjectExternalId(final String projectExternalId) {
        this.projectExternalId = projectExternalId;
    }

    public Revision getRevision() {
        return revision;
    }

    public void setRevision(final Revision revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("projectExternalId", projectExternalId)
                .append("revision", revision)
                .toString();
    }
}
