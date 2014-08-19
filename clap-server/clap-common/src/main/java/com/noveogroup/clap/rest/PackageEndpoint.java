package com.noveogroup.clap.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author Andrey Sokolov
 */
@Path("/apk")
public interface PackageEndpoint {

    @GET
    @Path("/{revId}/{variantId}")
    @Produces("application/vnd.android.package-archive")
    Response downloadAPK(@PathParam("revId") long revId,
                         @PathParam("variantId") long variantId, @QueryParam("token") String token);
}
