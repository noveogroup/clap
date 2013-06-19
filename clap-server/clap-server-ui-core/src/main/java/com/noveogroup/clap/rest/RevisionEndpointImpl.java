package com.noveogroup.clap.rest;


import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.controller.ProjectsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private RevisionService revisionsFacade;


    @Override
    public Revision createOrUpdateRevision(final CreateOrUpdateRevisionRequest request) {
        final Revision revision = new Revision();
        revision.setHash(request.getRevisionHash());
        final AddOrGetRevisionRequest addOrGetRevisionRequest = new AddOrGetRevisionRequest();
        addOrGetRevisionRequest.setProjectExternalId(request.getProjectExternalId());
        addOrGetRevisionRequest.setRevision(revision);
        //FIXME: byte[] IS SOOOOOOOO BAD!
        //addOrGetRevisionRequest.setMainPackage(request.getMainPackage());
        //addOrGetRevisionRequest.setSpecialPackage(request.getSpecialPackage());
        return revisionsFacade.addOrGetRevision(addOrGetRevisionRequest);

    }

    @Override
    public Revision getRevision(final RevisionRequest request) {
        return revisionsFacade.getRevision(request);
    }
}