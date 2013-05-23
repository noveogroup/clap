package com.noveogroup.clap.model.revision;

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
}
