package com.noveogroup.clap.model.revision;

import java.util.Arrays;

public class ApplicationFile {

    private String filename;
    private byte[] content;

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApplicationFile{");
        sb.append("filename='").append(filename).append('\'');
        sb.append(", content=").append(content != null ? content.length : "null");
        sb.append('}');
        return sb.toString();
    }
}
