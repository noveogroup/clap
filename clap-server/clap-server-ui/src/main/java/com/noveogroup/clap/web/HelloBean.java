package com.noveogroup.clap.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.noveogroup.clap.config.ConfigBean;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

    public StreamedContent getQRCode() {
        return QRCode;
    }
}
