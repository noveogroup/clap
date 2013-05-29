package com.noveogroup.clap.rest;

import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.facade.ProjectsFacade;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.rest.TestEndpoint;
import com.noveogroup.clap.service.project.ProjectService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TestEndpointImpl implements TestEndpoint {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Inject
    private ProjectsFacade projectsFacade;

    @Override
    public String echo(final String original) {
        final ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName("Name");
//        projectsFacade.save(project);
        return MAPPER.map(projectEntity, Project.class).getName();
    }

    @Override
    public Project getTestModifyProject(final Project project) {
        project.setName(project.getName() + "_ololo");
        return project;
    }
}
