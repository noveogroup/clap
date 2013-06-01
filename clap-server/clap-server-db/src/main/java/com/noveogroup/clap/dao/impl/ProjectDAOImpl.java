package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

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
    public ProjectEntity findProjectByExternalIdOrReturnNull(final String externalId) {
        final Query query = entityManager.createNamedQuery(GET_PROJECT_BY_EXTERNAL_ID);
        query.setParameter(GET_PROJECT_BY_EXTERNAL_ID_PARAMETER, externalId);
        List results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else if (results.size() == 1) {
            return (ProjectEntity) results.get(0);
        } else {
            throw new NonUniqueResultException();
        }

    }

}
