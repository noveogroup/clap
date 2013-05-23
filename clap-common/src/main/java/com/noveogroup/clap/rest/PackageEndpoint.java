package com.noveogroup.clap.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * @author Andrey Sokolov
 */
@Path("/downloadAPK")
public interface PackageEndpoint {

    @GET
    @Path("{id}/{type}")
    @Produces("application/vnd.android.package-archive")
    Response downloadAPK(@PathParam("id") long id, @PathParam("type") int type);
}
