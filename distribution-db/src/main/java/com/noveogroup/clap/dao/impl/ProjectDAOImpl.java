package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.entity.Project;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author
 */
@Stateless
public class ProjectDAOImpl extends GenericHibernateDAOImpl<Project, Long> implements ProjectDAO {

    private static final String GET_PROJECT_BY_NAME = "getProjectByName";
    private static final String GET_PROJECT_BY_NAME_PARAMETER = "name";

    @Override
    public Project findById(Long id) {
        Project project = super.findById(id);
        Hibernate.initialize(project.getRevisions());
        return project;
    }

    @Override
    public Project findProjectByName(String name) {
        Query query = entityManager.createNamedQuery(GET_PROJECT_BY_NAME);
        query.setParameter(GET_PROJECT_BY_NAME_PARAMETER,name);
        Project project = (Project) query.getSingleResult();
        Hibernate.initialize(project.getRevisions());
        return project;
    }

}
