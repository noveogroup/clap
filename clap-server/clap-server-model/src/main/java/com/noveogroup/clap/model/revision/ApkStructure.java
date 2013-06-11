package com.noveogroup.clap.model.revision;


import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class ApkStructure {
    private ApkEntry rootEntry;

    public ApkEntry getRootEntry() {
        return rootEntry;
    }

    public void setRootEntry(ApkEntry rootEntry) {
        this.rootEntry = rootEntry;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("rootEntry", rootEntry).
                toString();
    }
}
