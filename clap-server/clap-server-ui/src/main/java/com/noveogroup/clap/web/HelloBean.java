package com.noveogroup.clap.web;

import com.google.common.collect.Lists;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.service.project.ProjectService;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author
 */
@Named
@RequestScoped
public class HelloBean {

    @Inject
    private ProjectService projectsFacade;

    @Inject
    private ConfigBean configBean;

    private final StreamedContent QRCode;

    private String testValue;

    public HelloBean() throws IOException, WriterException {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        final Writer writer = new QRCodeWriter();
        //151 chars
        final BitMatrix matrix = writer.encode("123456789012345678901234567890123456789012345678901" +
                "234567890123456789012345678901234567890123456789012345678" +
                "901234567890123456789012345678901234567890_",
                BarcodeFormat.QR_CODE, 100, 100);
        MatrixToImageWriter.writeToStream(matrix, "PNG", buf);
        //422 bytes
        final byte[] bytes = buf.toByteArray();
        QRCode = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/png");
    }

    public StreamedContent getQRCode() {
        return QRCode;
    }

    public Collection<SelectItem> autocompleteMethod() {
        final List<SelectItem> ret = Lists.newArrayList();
        ret.add(new SelectItem(1, "111"));
        ret.add(new SelectItem(2, "222"));
        ret.add(new SelectItem(3, "333"));
        ret.add(new SelectItem(4, "444"));
        return ret;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(final String testValue) {
        this.testValue = testValue;
    }
}
