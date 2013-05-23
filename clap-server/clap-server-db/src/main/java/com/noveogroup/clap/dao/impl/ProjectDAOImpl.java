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

    private static final String GET_PROJECT_BY_EXTERNAL_ID = "getProjectByExternalId";
    private static final String GET_PROJECT_BY_EXTERNAL_ID_PARAMETER = "externalId";

    @Override
    public ProjectEntity findById(final Long id) {
        final ProjectEntity projectEntity = super.findById(id);
        if (projectEntity != null) {
            Hibernate.initialize(projectEntity.getRevisions());
        }
        return projectEntity;
    }

    @Override
    public ProjectEntity findProjectByExternalId(final String externalId) {
        final Query query = entityManager.createNamedQuery(GET_PROJECT_BY_EXTERNAL_ID);
        query.setParameter(GET_PROJECT_BY_EXTERNAL_ID_PARAMETER, externalId);
        final ProjectEntity projectEntity = (ProjectEntity) query.getSingleResult();
        if (projectEntity != null) {
            Hibernate.initialize(projectEntity.getRevisions());
        }
        return projectEntity;
    }

}
