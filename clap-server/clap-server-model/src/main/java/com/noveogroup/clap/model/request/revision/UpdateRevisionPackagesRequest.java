package com.noveogroup.clap.model.request.revision;


import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author Andrey Sokolov
 */
public class UpdateRevisionPackagesRequest extends BaseRevisionPackagesRequest{
    private String revisionHash;

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("revisionHash", revisionHash)
                .toString();
    }
}
