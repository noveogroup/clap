package com.noveogroup.clap.model.revision;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class RevisionWithApkStructure extends Revision {

    private List<RevisionVariantWithApkStructure> variantWithApkStructureList;

    public List<RevisionVariantWithApkStructure> getVariantWithApkStructureList() {
        return variantWithApkStructureList;
    }

    public void setVariantWithApkStructureList(
            final List<RevisionVariantWithApkStructure> variantWithApkStructureList) {
        this.variantWithApkStructureList = variantWithApkStructureList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("variantWithApkStructureList", variantWithApkStructureList)
                .toString();
    }
}
