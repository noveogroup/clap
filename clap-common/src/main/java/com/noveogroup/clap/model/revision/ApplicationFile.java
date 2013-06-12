package com.noveogroup.clap.model.revision;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;

public class ApplicationFile {

    private String filename;

    private File content;

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public File getContent() {
        return content;
    }

    public void setContent(final File content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("filename", filename)
                .append("content", content)
                .toString();
    }
}
