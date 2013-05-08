package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.entity.Project;

import javax.ejb.Stateless;

/**
 * @author
 */
@Stateless
public class ProjectDAOImpl extends GenericHibernateDAOImpl<Project, Long> implements ProjectDAO {
}
