package com.noveogroup.clap.rest;


import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.StreamedPackage;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.rest.exception.ClapException;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.service.tempfiles.TempFileService;
import com.noveogroup.clap.web.controller.ProjectsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionEndpointImpl.class);

    @Inject
    private RevisionService revisionService;

    @Inject
    private TempFileService tempFileService;

    @Inject
    private AuthenticationRequestHelper authenticationRequestHelper;

    @Override
    public Revision createOrUpdateRevision(final CreateOrUpdateRevisionRequest request) {
        final Authentication authentication = new Authentication();
        authentication.setUser(request.getUser());
        authenticationRequestHelper.applyAuthentication(authentication);
        final Revision revision = new Revision();
        revision.setHash(request.getRevisionHash());
        final AddOrGetRevisionRequest addOrGetRevisionRequest = new AddOrGetRevisionRequest();
        addOrGetRevisionRequest.setProjectExternalId(request.getProjectExternalId());
        addOrGetRevisionRequest.setRevision(revision);
        addOrGetRevisionRequest.setMainPackage(createStreamedPackage(request.getMainPackage()));
        addOrGetRevisionRequest.setSpecialPackage(createStreamedPackage(request.getSpecialPackage()));
        return revisionService.addOrGetRevision(addOrGetRevisionRequest);

    }

    private StreamedPackage createStreamedPackage(final InputStream stream) {
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

    private StreamedPackage createStreamedPackage(final byte[] data) {
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

    @Override
    public Revision getRevision(final RevisionRequest request) {
        return revisionService.getRevision(request);
    }
}