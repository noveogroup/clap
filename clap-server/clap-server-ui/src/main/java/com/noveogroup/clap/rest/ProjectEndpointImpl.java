package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.rest.ProjectEndpoint;
import com.noveogroup.clap.service.project.ProjectService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class ProjectEndpointImpl implements ProjectEndpoint {

    private static Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectService;


    @Override
    public Project createProject(final Project project) {
        //TODO authentication
        return projectService.createProject(null,project);
    }

    @Override
    public Project getProject(@QueryParam("id") final Long id) {
        //TODO authentication
        return projectService.findById(null,id);
    }
}
