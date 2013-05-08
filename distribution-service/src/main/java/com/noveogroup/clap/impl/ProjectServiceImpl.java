package com.noveogroup.clap.impl;

import com.noveogroup.clap.ProjectService;
import com.noveogroup.clap.dao.ProjectDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * @author
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@Stateless
public class ProjectServiceImpl implements ProjectService {


    @EJB
    private ProjectDAO projectDAO;

    @Override
    public String getName() {
        projectDAO.selectAll();
        return "Mikhail";
    }
}
