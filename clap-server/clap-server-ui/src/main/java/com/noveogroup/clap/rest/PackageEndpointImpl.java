package com.noveogroup.clap.rest;

import com.noveogroup.clap.facade.RevisionsFacade;
import com.noveogroup.clap.model.request.revision.GetApplicationRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class PackageEndpointImpl implements PackageEndpoint {

    @Inject
    private RevisionsFacade revisionsFacade;

    @Override
    public Response downloadAPK(final long id, final int type) {
        final GetApplicationRequest request = new GetApplicationRequest();
        request.setRevisionId(id);
        request.setApplicationType(type == 0 ? ApplicationType.MAIN : ApplicationType.SPECIAL);
        final ApplicationFile application = revisionsFacade.getApplication(request);
        return Response.ok(application.getContent()).header("Content-Disposition",
                "attachment; filename=\""+application.getFilename()+"\"").build();
    }
}
