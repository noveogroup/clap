package com.noveogroup.clap.service.tempfiles;

import com.noveogroup.clap.config.ConfigBean;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class TempFileService {

    @Inject
    private ConfigBean configBean;
    private String tempFilesDir;

    @PostConstruct
    protected void setup(){
        tempFilesDir = configBean.getTempFilesDir();
    }

    public File createTempFile() throws IOException {
        return File.createTempFile("clap_","",new File(tempFilesDir));
    }

    public File createTempFile(final InputStream content) throws IOException {
        final File file = createTempFile();
        final OutputStream outputStream = new FileOutputStream(file);
        IOUtils.copy(content, outputStream);
        outputStream.flush();
        content.close();
        outputStream.close();
        return file;
    }
}
