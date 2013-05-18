package com.noveogroup.clap.web.util.init;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.revision.Revision;
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
        Project project = new Project();
        project.setName("test_project");
        project.setCreationDate(new Date(1368308474));
        project = projectService.createProject(project);
        Revision revision = new Revision();
        revision.setTimestamp(1368318776L);
        revisionService.addRevision(project.getId(), revision,null,null);
    }

    public void fakeMethod(){

    }
}
