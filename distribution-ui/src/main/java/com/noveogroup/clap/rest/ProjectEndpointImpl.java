package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.ProjectDTO;
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
    public ProjectDTO createProject(final ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    @Override
    public ProjectDTO getProject(@QueryParam("id") final Long id) {
        return projectService.findById(id);
    }
}
