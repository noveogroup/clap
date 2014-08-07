package com.noveogroup.clap.model.revision;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class RevisionWithApkStructure extends Revision {
    private ApkStructure apkStructure;

    public ApkStructure getApkStructure() {
        return apkStructure;
    }

    public void setApkStructure(final ApkStructure apkStructure) {
        this.apkStructure = apkStructure;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("apkStructure", apkStructure).
                toString();
    }
}
