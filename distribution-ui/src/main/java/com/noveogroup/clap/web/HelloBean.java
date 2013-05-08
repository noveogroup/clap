package com.noveogroup.clap.web;

import com.noveogroup.clap.service.ProjectService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author
 */
@Named
@Stateless
public class HelloBean {

    private static final Logger LOG = LoggerFactory.getLogger(HelloBean.class);

    @Inject
    private ProjectService projectService;


    public String getGreeting() {
        LOG.trace("Hello World!");
        LOG.debug("How are you today?");
        LOG.info("I am fine.");
        LOG.warn("I love programming.");
        LOG.error("I am programming.");
        return "Hello World!" + projectService.getName();
    }
}
