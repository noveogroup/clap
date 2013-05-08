package com.noveogroup.clap.rest;

import com.noveogroup.ProjectService;
import com.noveogroup.clap.entity.Project;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@ApplicationScoped
@Path("/")
public class TestController {

    private static Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectService;

    @GET
    @Path("echo")
    public String echo(@QueryParam("q") String original) {
        Project project = new Project();
        project.setName("Name");
        return MAPPER.map(project, com.noveogroup.clap.model.ProjectDTO.class).getName();
    }
}
