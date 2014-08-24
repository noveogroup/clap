package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.Project;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Mikhail Demidov
 */
@Path("/project")
public interface ProjectEndpoint {


    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Project createProject(Project project);

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    Project getProject(@QueryParam("id") Long id);

    @GET
    @Path("/icon/{id}")
    Response getProjectIcon(@PathParam("id") long projectId);

}
