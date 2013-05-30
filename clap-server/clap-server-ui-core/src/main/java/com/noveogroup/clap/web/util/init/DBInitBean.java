package com.noveogroup.clap.web.util.init;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;

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
        project.setExternalId("test_ext_id");
        project.setCreationDate(new Date(1368308474));
        project = projectService.createProject(project);
        final Revision revision = new Revision();
        revision.setTimestamp(1368318776L);
        final AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        request.setProjectExternalId("test_ext_id");
        request.setRevision(revision);
        revisionService.addOrGetRevision(request);
    }

    public void fakeMethod(){

    }
}