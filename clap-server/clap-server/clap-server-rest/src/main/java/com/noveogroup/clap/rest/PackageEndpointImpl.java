package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.service.revision.RevisionService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class PackageEndpointImpl extends BaseEndpoint implements PackageEndpoint {

    @Inject
    private RevisionService revisionService;

    @Override
    public Response downloadAPK(final long revId, final long variantId, final String token) {
        login(token);
        final ApplicationFile application = revisionService.getApplication(revId, variantId);
        return Response.ok(application.getContent()).header("Content-Disposition",
                "attachment; filename=\"" + application.getFilename() + "\"").build();
    }

}
