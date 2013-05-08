package com.noveogroup.dao.impl;

import com.noveogroup.dao.ProjectDAO;
import com.noveogroup.entity.Project;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * @author
 */
@Stateless
public class ProjectDAOImpl extends GenericHibernateDAOImpl<Project, Long> implements ProjectDAO {
}
