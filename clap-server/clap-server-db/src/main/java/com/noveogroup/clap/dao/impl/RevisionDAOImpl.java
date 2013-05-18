package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class RevisionDAOImpl extends GenericHibernateDAOImpl<RevisionEntity, Long> implements RevisionDAO {

    private static final String REVISION_BY_TIMESTAMP = "getRevisionByTimestamp";
    private static final String REVISION_BY_TIMESTAMP_PARAMETER = "timestamp";

    @Override
    public RevisionEntity getRevisionByTimestamp(Long timestamp) {
        Query query = entityManager.createNamedQuery(REVISION_BY_TIMESTAMP);

        query.setParameter(REVISION_BY_TIMESTAMP_PARAMETER, timestamp);
        RevisionEntity revisionEntity = (RevisionEntity) query.getSingleResult();
        Hibernate.initialize(revisionEntity.getMessages());
        return revisionEntity;
    }

    @Override
    public RevisionEntity findById(Long aLong) {
        RevisionEntity revisionEntity = super.findById(aLong);
        Hibernate.initialize(revisionEntity.getMessages());
        return revisionEntity;
    }
}
