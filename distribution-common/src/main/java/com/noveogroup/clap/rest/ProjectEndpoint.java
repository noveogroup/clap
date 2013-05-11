package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.ProjectDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Mikhail Demidov
 */
@Path("/project")
public interface ProjectEndpoint {


    @POST
    @Path("createProject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ProjectDTO createProject(ProjectDTO projectDTO);

    @GET
    @Path("getProject")
    @Produces(MediaType.APPLICATION_JSON)
    ProjectDTO getProject(@QueryParam("id") Long id);


}
