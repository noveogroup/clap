package com.noveogroup.clap.service.file;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.exception.ClapTempFilesException;
import com.noveogroup.clap.model.file.FileType;
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

    @Inject
    private ConfigBean configBean;


    @Override
    public File saveFile(final FileType fileType, final InputStream content) {
        return saveFile(fileType, content, null);
    }

    @Override
    public File saveFile(final FileType fileType, final InputStream content, final String namePrefix) {
        return saveFile(fileType, content, namePrefix, null);
    }

    @Override
    public File saveFile(final FileType fileType, final InputStream content,
                         final String namePrefix, final String nameSuffix) {
        List<String> filesDirs;
        switch (fileType) {
            case TEMP:
                filesDirs = configBean.getTempFilesDirs();
                break;
            case SCREENSHOT:
                filesDirs = configBean.getScreenshotFilesDirs();
                break;
            case APK:
                filesDirs = configBean.getApkFilesDirs();
                break;
            default:
                throw new IllegalArgumentException("unknown filetype :" + fileType);
        }
        return createFile(content, filesDirs, namePrefix, nameSuffix);
    }

    @Override
    public File getFile(final String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    @Override
    public boolean removeFile(final String path) {
        if(path != null){
            final File file = new File(path);
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    private File createFile(final String dir, final String namePrefix, final String nameSuffix) throws IOException {
        return File.createTempFile(
                namePrefix != null ? namePrefix : "clap_",
                nameSuffix != null ? nameSuffix : "",
                new File(dir));
    }

    private File createFile(final InputStream content, final List<String> filesDirs,
                            final String namePrefix, final String nameSuffix) {
        List<IOException> exceptions = Lists.newArrayList();
        for (String directory : filesDirs) {
            try {
                final File file = createFile(directory, namePrefix, nameSuffix);
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
