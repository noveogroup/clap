package com.noveogroup.clap.model.test;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

/**
 * @author Mikhail Demidov
 */
public class TestMultipart {


    private byte[] data;

    public TestMultipart() {
    }

    public byte[] getData() {
        return data;
    }

    @FormParam("filedata")
    @PartType("application/octet-stream")
    public void setData(final byte[] data) {
        this.data = data;
    }
}
