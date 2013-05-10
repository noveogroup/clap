package com.noveogroup.clap.service.impl;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.service.ProjectService;
import com.noveogroup.clap.dao.ProjectDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

/**
 * @author Mikhail Demidov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({TransactionInterceptor.class})
public class ProjectServiceImpl implements ProjectService {


    @EJB
    private ProjectDAO projectDAO;

    @Override
    public String getName() {
        projectDAO.selectAll();
        return "Mikhail";
    }

    @Override
    public Project save(final Project project) {
        return projectDAO.persist(project);
    }
}
