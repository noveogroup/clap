package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.ProjectDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Andrey Sokolov
 */
@Path("/")
public interface TestController {

    @GET
    @Path("echo")
    String echo(@QueryParam("q") String original);

    @POST
    @Path("testModifyProject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ProjectDTO getTestModifyProject(ProjectDTO projectDTO);
}
