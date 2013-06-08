package com.noveogroup.clap.model.request.revision;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.InputStream;

/**
 * @author Andrey Sokolov
 */
public class StreamedPackage {

    private InputStream stream;
    private long length;

    public StreamedPackage(InputStream stream, long length) {
        this.stream = stream;
        this.length = length;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("length", length)
                .toString();
    }
}
