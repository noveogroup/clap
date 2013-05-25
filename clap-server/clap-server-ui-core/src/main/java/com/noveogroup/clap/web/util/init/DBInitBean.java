package com.noveogroup.clap.web.util.init;

import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.init.InitService;
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

    @Inject
    private InitService initService;

    @PostConstruct
    protected void initDB(){
        initService.initDB();
    }

    public void fakeMethod(){

    }
}
