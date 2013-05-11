package com.noveogroup.clap.web.util.init;

import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Named
@ApplicationScoped
public class DBInitBean {

    @Inject
    private ProjectService projectService;

    @Inject
    private RevisionService revisionService;

    @PostConstruct
    protected void initDB(){
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("test_project");
        projectDTO.setCreationDate(new Date(1368318474));
        projectDTO = projectService.createProject(projectDTO);
        RevisionDTO revisionDTO = new RevisionDTO();
        revisionDTO.setTimestamp(new Date(1368318776));
        revisionService.addRevision(projectDTO.getId(),revisionDTO,null,null);
    }

    public void fakeMethod(){

    }
}
