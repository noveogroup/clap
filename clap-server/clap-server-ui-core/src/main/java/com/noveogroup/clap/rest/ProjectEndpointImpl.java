package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.Project;
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

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectsFacade;


    @Override
    public Project createProject(final Project project) {
        return projectsFacade.createProject(project);
    }

    @Override
    public Project getProject(@QueryParam("id") final Long id) {
        return projectsFacade.findById(id);
    }
}
