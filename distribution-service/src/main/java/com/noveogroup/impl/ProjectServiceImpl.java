package com.noveogroup.impl;

import com.noveogroup.ProjectService;

import javax.ejb.LocalBean;

/**
 * @author
 */
@LocalBean
public class ProjectServiceImpl implements ProjectService {


    @Override
    public String getName() {
        return "Mikhail";
    }
}
