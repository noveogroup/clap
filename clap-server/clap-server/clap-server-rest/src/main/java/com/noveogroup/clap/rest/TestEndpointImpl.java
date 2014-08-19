package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.service.project.ProjectService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TestEndpointImpl implements TestEndpoint {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectService projectService;

    @Override
    public String echo(final String original) {
        final Project projectEntity = new Project();
        projectEntity.setName(original);
        return projectEntity.getName();
    }

    @Override
    public Project getTestModifyProject(final Project project) {
        project.setName(project.getName() + "_ololo");
        return project;
    }
}
