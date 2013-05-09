package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.ProjectDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author Andrey Sokolov
 */
public interface TestController {
    @GET
    @Path("echo")
    String echo(@QueryParam("q") String original);

    @GET
    @Path("testProject")
    @Produces(MediaType.APPLICATION_JSON)
    ProjectDTO getTestProject(@QueryParam("q") String name);
}
