package com.noveogroup.clap.rest;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.service.ProjectService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class ProjectControllerImpl implements ProjectController {

    private static Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectService;


    @Override
    public ProjectDTO createProject(final ProjectDTO projectDTO) {
        Project project = MAPPER.map(projectDTO, Project.class);
        project = projectService.createProject(project);
        return MAPPER.map(project, ProjectDTO.class);
    }

    @Override
    public ProjectDTO getProject(@QueryParam("id") final Long id) {
        Project project = projectService.findById(id);
        return MAPPER.map(project, ProjectDTO.class);
    }
}
