package com.noveogroup.clap.model.request.message;

import com.noveogroup.clap.model.request.BaseRequest;


/**
 * @author Andrey Sokolov
 */
public class BaseMessageRequest extends BaseRequest {
    private String revisionHash;
    private String variantHash;

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
}
