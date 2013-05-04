package com.noveogroup;

import com.noveogroup.test.Test;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author
 */
@Named
@Stateless
public class HelloBean {

    @Inject
    private ProjectService projectService;

    public String getGreeting() {
        return "Hello World!" + projectService.getName();
    }
}
