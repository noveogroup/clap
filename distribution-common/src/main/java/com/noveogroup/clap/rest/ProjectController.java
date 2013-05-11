package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.ProjectDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Mikhail Demidov
 */
@Path("/project")
public interface ProjectController {


    @POST
    @Path("createController")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ProjectDTO createProject(ProjectDTO projectDTO);


}
