package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.entity.Project;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;

/**
 * @author
 */
@Stateless
public class ProjectDAOImpl extends GenericHibernateDAOImpl<Project, Long> implements ProjectDAO {

    @Override
    public Project findById(Long id) {
        Project project = super.findById(id);
        Hibernate.initialize(project.getRevisions());
        return project;
    }
}
