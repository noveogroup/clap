package com.noveogroup.clap.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noveogroup.clap.service.ProjectService;

import com.noveogroup.clap.web.util.config.ConfigBean;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;

/**
 * @author
 */
@Named
@RequestScoped
public class HelloBean {

    private static final Logger LOG = LoggerFactory.getLogger(HelloBean.class);

    @Inject
    private ProjectService projectService;

    @Inject
    private ConfigBean configBean;

    private StreamedContent QRCode;

    public HelloBean() throws IOException, WriterException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        Writer writer = new QRCodeWriter();
        //151 chars
        BitMatrix matrix = writer.encode("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890_",
                BarcodeFormat.QR_CODE, 100, 100);
        MatrixToImageWriter.writeToStream(matrix, "PNG", buf);
        //422 bytes
        byte[] bytes = buf.toByteArray();
        QRCode = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/png");
    }

    public String getGreeting() {
        LOG.trace("Hello World!");
        LOG.debug("How are you today?");
        LOG.info("I am fine.");
        LOG.warn("I love programming.");
        LOG.error("I am programming.");
        return "Hello World!" + projectService.getName()+" " + configBean.getTestProperty();
    }

    public StreamedContent getQRCode() {
        return QRCode;
    }
}
