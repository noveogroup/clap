package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.revision.StreamedPackage;
import com.noveogroup.clap.rest.auth.ClapRestAuthenticationToken;
import com.noveogroup.clap.rest.exception.ClapException;
import com.noveogroup.clap.service.tempfiles.TempFileService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEndpoint.class);

    @Inject
    private TempFileService tempFileService;

    protected void login(final String token) {
        SecurityUtils.getSubject().login(new ClapRestAuthenticationToken(token));
    }

    protected StreamedPackage createStreamedPackage(final InputStream stream) {
        if (stream != null) {
            try {
                final File tempFile = tempFileService.createTempFile(stream);
                final long length = tempFile.length();
                final StreamedPackage streamedPackage = new StreamedPackage(new FileInputStream(tempFile), length);
                LOGGER.debug("Retrieved package as stream: " + tempFile.getName() + "; length: " + length);
                return streamedPackage;
            } catch (IOException e) {
                throw new ClapException(e);
            }
        }
        return null;
    }

    protected StreamedPackage createStreamedPackage(final byte[] data) {
        if (data != null) {
            try {
                final File tempFile = tempFileService.createTempFile(new ByteArrayInputStream(data));
                final long length = tempFile.length();
                final StreamedPackage streamedPackage = new StreamedPackage(new FileInputStream(tempFile), length);
                LOGGER.debug("Retrieved package as byte[]: " + tempFile.getName() + "; length: " + length);
                return streamedPackage;
            } catch (IOException e) {
                throw new ClapException(e);
            }
        }
        return null;
    }
}
