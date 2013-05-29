package com.noveogroup.clap.web.util.init;

import com.noveogroup.clap.facade.init.InitService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
