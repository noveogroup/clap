package com.noveogroup.clap.model.test;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

/**
 * @author Mikhail Demidov
 */
public class TestMultipart {

    @FormParam("filedata")
    @PartType("application/octet-stream")
    private byte[] data;

    public TestMultipart() {
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }
}
