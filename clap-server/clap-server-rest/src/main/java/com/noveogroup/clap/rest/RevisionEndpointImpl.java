package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.revision.RevisionService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
        return revisionService.addOrGetRevision(request);

    }

    @Override
    public Revision getRevision(final RevisionRequest request) {
        login(request.getToken());
        return revisionService.getRevision(request.getRevisionId());
    }
}