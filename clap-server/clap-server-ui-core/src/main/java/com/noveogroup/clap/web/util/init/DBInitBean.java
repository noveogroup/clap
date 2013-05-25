package com.noveogroup.clap.web.util.init;

import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Named
@ApplicationScoped
public class DBInitBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBInitBean.class);

    @Inject
    private ProjectService projectService;

    @Inject
    private RevisionService revisionService;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationRequestHelper authenticationRequestHelper;

    @PostConstruct
    protected void initDB(){

        User user = new User();
        user.setLogin("test");
        user.setPassword("123");
        user = userService.createUser(user);
        LOGGER.debug("user created = " + user);

        final Authentication authentication = new Authentication();
        authentication.setUser(user);
        authenticationRequestHelper.applyAuthentication(authentication);

        Project project = new Project();
        project.setName("test_project");
        project.setExternalId("test_ext_id");
        project.setCreationDate(new Date(1368308474));
        project = projectService.createProject(project);

        LOGGER.debug("project created : " + project);

        Revision revision = new Revision();
        revision.setTimestamp(1368318776L);
        final AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        request.setProjectExternalId(project.getExternalId());
        request.setRevision(revision);
        revision = revisionService.addOrGetRevision(request);

        LOGGER.debug("revision created : " + revision);
    }

    public void fakeMethod(){

    }
}
