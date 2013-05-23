package com.noveogroup.clap.model.request.revision;

import com.noveogroup.clap.model.request.BaseRequest;

/**
 * @author Andrey Sokolov
 */
public class RevisionRequest extends BaseRequest{
    private Long revisionId;

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(final Long revisionId) {
        this.revisionId = revisionId;
    }
}
