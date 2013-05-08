package com.noveogroup.clap.rest;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.service.ProjectService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/")
public class TestControllerImpl implements TestController {

    private static Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectService;

    @Override
    @GET
    @Path("echo")
    public String echo(@QueryParam("q") String original) {
        Project project = new Project();
        project.setName("Name");
        return MAPPER.map(project, com.noveogroup.clap.model.ProjectDTO.class).getName();
    }

    @Override
    @GET
    @Path("testProject")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO getTestProject(@QueryParam("q") String name) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(name);
        return projectDTO;
    }
}
