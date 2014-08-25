package com.noveogroup.clap.rest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noveogroup.clap.rest.exception.ClapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class MiscEndpointImpl extends BaseEndpoint implements MiscEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiscEndpointImpl.class);

    @Override
    public Response getQRCode(final String url) throws IOException {

        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        final Writer writer = new QRCodeWriter();
        final BitMatrix matrix;
        try {
            matrix = writer.encode(url, BarcodeFormat.QR_CODE, 100, 100);
        } catch (WriterException e) {
            throw new ClapException(e);
        }
        MatrixToImageWriter.writeToStream(matrix, "PNG", buf);
        final byte[] bytes = buf.toByteArray();
        LOGGER.debug("qrcode for " + url + " generated");
        return returnImage(new ByteArrayInputStream(bytes), "qrcode.png");
    }
}
