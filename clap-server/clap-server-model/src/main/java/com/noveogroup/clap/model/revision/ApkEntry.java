package com.noveogroup.clap.model.revision;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class ApkEntry {

    /**
     * without path
     */
    private String fileName;

    /**
     * zip entry name - with full path
     */
    private String entryName;

    private List<ApkEntry> innerEntries;

    private boolean directory;

    public List<ApkEntry> getInnerEntries() {
        return innerEntries;
    }

    public void setInnerEntries(final List<ApkEntry> innerEntries) {
        this.innerEntries = innerEntries;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(final String entryName) {
        this.entryName = entryName;
    }

    public boolean isDirectory() {
        return directory;
    }

    /**
     * define if entry is directory
     *
     * @param directory
     */
    public void setDirectory(final boolean directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("fileName", fileName).
                append("entryName", entryName).
                append("innerEntries", innerEntries).
                append("directory", directory).
                toString();
    }
}
