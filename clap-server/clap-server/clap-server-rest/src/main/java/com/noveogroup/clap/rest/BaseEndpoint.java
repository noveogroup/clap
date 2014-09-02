package com.noveogroup.clap.rest;

import com.google.common.collect.Lists;
import com.noveogroup.clap.exception.ClapTempFilesException;
import com.noveogroup.clap.model.file.FileType;
import com.noveogroup.clap.model.revision.StreamedPackage;
import com.noveogroup.clap.rest.auth.ClapRestAuthenticationToken;
import com.noveogroup.clap.service.file.FileService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEndpoint.class);

    @Inject
    private FileService fileService;

    protected void login(final String token) {
        SecurityUtils.getSubject().login(new ClapRestAuthenticationToken(token));
    }

    protected StreamedPackage createStreamedPackage(final InputStream stream) {
        if (stream != null) {
            try {
                final File tempFile = fileService.saveFile(FileType.TEMP,stream);
                final long length = tempFile.length();
                final StreamedPackage streamedPackage = new StreamedPackage(new FileInputStream(tempFile), length);
                LOGGER.debug("Retrieved package as stream: " + tempFile.getName() + "; length: " + length);
                return streamedPackage;
            } catch (IOException e) {
                List<IOException> exceptions = Lists.newArrayList(e);
                throw new ClapTempFilesException(exceptions);
            }
        }
        return null;
    }

    protected StreamedPackage createStreamedPackage(final byte[] data) {
        if (data != null) {
            try {
                final File tempFile = fileService.saveFile(FileType.TEMP,new ByteArrayInputStream(data));
                final long length = tempFile.length();
                final StreamedPackage streamedPackage = new StreamedPackage(new FileInputStream(tempFile), length);
                LOGGER.debug("Retrieved package as byte[]: " + tempFile.getName() + "; length: " + length);
                return streamedPackage;
            } catch (IOException e) {
                List<IOException> exceptions = Lists.newArrayList(e);
                throw new ClapTempFilesException(exceptions);
            }
        }
        return null;
    }

    protected Response returnImage(final File imageFile) {
        if (imageFile != null) {
            return Response.ok(imageFile).header("Content-Disposition",
                    "attachment; filename=\"" + imageFile.getName() + "\"").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    protected Response returnImage(final InputStream stream,final String fileName) {
        if (stream != null) {
            return Response.ok(stream).header("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
