package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;
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
    public Response downloadAPK(final long id, final int type, final String token) {
        final ApplicationType applicationType = type == 0 ? ApplicationType.MAIN : ApplicationType.SPECIAL;
        login(token);
        final ApplicationFile application = revisionService.getApplication(id, applicationType);
        return Response.ok(application.getContent()).header("Content-Disposition",
                "attachment; filename=\"" + application.getFilename() + "\"").build();
    }
}
