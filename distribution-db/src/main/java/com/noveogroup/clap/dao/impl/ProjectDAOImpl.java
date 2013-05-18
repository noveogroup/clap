package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author
 */
@Stateless
public class ProjectDAOImpl extends GenericHibernateDAOImpl<ProjectEntity, Long> implements ProjectDAO {

    private static final String GET_PROJECT_BY_NAME = "getProjectByName";
    private static final String GET_PROJECT_BY_NAME_PARAMETER = "name";

    @Override
    public ProjectEntity findById(Long id) {
        ProjectEntity projectEntity = super.findById(id);
        Hibernate.initialize(projectEntity.getRevisions());
        return projectEntity;
    }

    @Override
    public ProjectEntity findProjectByName(String name) {
        Query query = entityManager.createNamedQuery(GET_PROJECT_BY_NAME);
        query.setParameter(GET_PROJECT_BY_NAME_PARAMETER,name);
        ProjectEntity projectEntity = (ProjectEntity) query.getSingleResult();
        Hibernate.initialize(projectEntity.getRevisions());
        return projectEntity;
    }

}
