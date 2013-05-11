package com.noveogroup.clap.rest;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.service.project.ProjectService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TestEndpointImpl implements TestEndpoint {

    private static Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectService;

    @Override
    public String echo(String original) {
        Project project = new Project();
        project.setName("Name");
//        projectService.save(project);
        return MAPPER.map(project, com.noveogroup.clap.model.ProjectDTO.class).getName();
    }

    @Override
    public ProjectDTO getTestModifyProject(ProjectDTO projectDTO) {
        projectDTO.setName(projectDTO.getName() + "_ololo");
        return projectDTO;
    }
}
