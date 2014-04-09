package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.request.BaseRequest;

/**
 * @author Andrey Sokolov
 */
public class BaseMessageRequest extends BaseRequest {
    protected String revisionHash;

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }
}
