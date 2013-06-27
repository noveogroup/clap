package com.noveogroup.clap.rest;


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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private RevisionService revisionService;

    @Inject
    private TempFileService tempFileService;

    @Override
    public Revision createOrUpdateRevision(final CreateOrUpdateRevisionRequest request) {
        final Revision revision = new Revision();
        revision.setHash(request.getRevisionHash());
        final AddOrGetRevisionRequest addOrGetRevisionRequest = new AddOrGetRevisionRequest();
        addOrGetRevisionRequest.setProjectExternalId(request.getProjectExternalId());
        addOrGetRevisionRequest.setRevision(revision);
        addOrGetRevisionRequest.setMainPackage(createStreamedPackage(request.getMainPackage()));
        addOrGetRevisionRequest.setSpecialPackage(createStreamedPackage(request.getSpecialPackage()));
        return revisionService.addOrGetRevision(addOrGetRevisionRequest);

    }

    private StreamedPackage createStreamedPackage(InputStream stream) {
        if (stream != null) {
            try {
                File tempFile = tempFileService.createTempFile(stream);
                final long length = tempFile.length();
                StreamedPackage streamedPackage = new StreamedPackage(new FileInputStream(tempFile), length);
                LOGGER.debug("Retrieved package: " + tempFile.getName() + "; length: " + length);
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