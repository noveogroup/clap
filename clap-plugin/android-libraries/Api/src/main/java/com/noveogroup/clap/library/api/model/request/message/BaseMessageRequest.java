package com.noveogroup.clap.library.api.model.request.message;

import com.noveogroup.clap.library.api.model.request.BaseRequest;

public class BaseMessageRequest extends BaseRequest {
    protected String revisionHash;

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }
}
