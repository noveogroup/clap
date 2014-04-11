package com.noveogroup.clap.service.file;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.exception.ClapTempFilesException;
import org.apache.commons.io.IOUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class FileServiceImpl implements FileService {

    //TODO

    @Inject
    private ConfigBean configBean;

    private File createTempFile(final String dir) throws IOException {
        return File.createTempFile("clap_", "", new File(dir));
    }

    @Override
    public File saveFile(final InputStream content) {
        return null;
    }

    @Override
    public File saveFile(final InputStream content, final String namePrefix) {
        return null;
    }

    @Override
    public File getFile(final String path) {
        return null;
    }

    @Override
    public boolean removeFile(final String path) {
        return false;
    }

    public File createTempFile(final InputStream content) {
        final List<String> tempFilesDirs = configBean.getTempFilesDirs();
        List<IOException> exceptions = Lists.newArrayList();
        for (String tempFilesDir : tempFilesDirs) {
            try {
                final File file = createTempFile(tempFilesDir);
                final OutputStream outputStream = new FileOutputStream(file);
                IOUtils.copy(content, outputStream);
                outputStream.flush();
                content.close();
                outputStream.close();
                return file;
            } catch (IOException e) {
                exceptions.add(e);
            }
        }
        throw new ClapTempFilesException(exceptions);
    }
}
