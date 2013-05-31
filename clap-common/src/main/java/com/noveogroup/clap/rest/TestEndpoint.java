package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.Project;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
