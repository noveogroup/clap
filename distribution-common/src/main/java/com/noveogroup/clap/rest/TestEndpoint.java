package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.Project;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Andrey Sokolov
 */
@Path("/")
public interface TestEndpoint {

    @GET
    @Path("echo")
    String echo(@QueryParam("q") String original);

    @POST
    @Path("testModifyProject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Project getTestModifyProject(Project project);
}
