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
    public ProjectEntity findById(final Long id) {
        final ProjectEntity projectEntity = super.findById(id);
        Hibernate.initialize(projectEntity.getRevisions());
        return projectEntity;
    }

    @Override
    public ProjectEntity findProjectByExternalId(final String externalId) {
        //TODO check if name can be external id
        final Query query = entityManager.createNamedQuery(GET_PROJECT_BY_NAME);
        query.setParameter(GET_PROJECT_BY_NAME_PARAMETER,externalId);
        final ProjectEntity projectEntity = (ProjectEntity) query.getSingleResult();
        Hibernate.initialize(projectEntity.getRevisions());
        return projectEntity;
    }

}
