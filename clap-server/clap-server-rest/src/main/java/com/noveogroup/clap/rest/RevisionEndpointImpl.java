package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.StreamedPackage;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.rest.exception.ClapException;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.service.tempfiles.TempFileService;
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
public class RevisionEndpointImpl extends BaseEndpoint implements RevisionEndpoint {

    @Inject
    private RevisionService revisionService;



    @Override
    public Revision createOrUpdateRevision(final CreateOrUpdateRevisionRequest request) {
        login(request.getToken());
        final Revision revision = new Revision();
        revision.setHash(request.getRevisionHash());
        final AddOrGetRevisionRequest addOrGetRevisionRequest = new AddOrGetRevisionRequest();
        addOrGetRevisionRequest.setProjectExternalId(request.getProjectExternalId());
        addOrGetRevisionRequest.setRevision(revision);
        addOrGetRevisionRequest.setMainPackage(createStreamedPackage(request.getMainPackage()));
        addOrGetRevisionRequest.setSpecialPackage(createStreamedPackage(request.getSpecialPackage()));
        return revisionService.addOrGetRevision(addOrGetRevisionRequest);

    }

    @Override
    public Revision getRevision(final RevisionRequest request) {
        login(request.getToken());
        return revisionService.getRevision(request.getRevisionId());
    }
}