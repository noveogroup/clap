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

    private static final String REVISION_BY_HASH = "getRevisionByHash";
    private static final String REVISION_BY_HASH_PARAMETER = "hash";

    @Override
    public RevisionEntity getRevisionByHash(final String revisionHash) {
        final Query query = entityManager.createNamedQuery(REVISION_BY_HASH);

        query.setParameter(REVISION_BY_HASH_PARAMETER, revisionHash);
        final RevisionEntity revisionEntity = (RevisionEntity) query.getSingleResult();
        if (revisionEntity != null) {
            Hibernate.initialize(revisionEntity.getMessages());
        }
        return revisionEntity;
    }

    @Override
    public RevisionEntity getRevisionByHashOrNull(final String revisionHash) {
        final Query query = entityManager.createNamedQuery(REVISION_BY_HASH);
        query.setParameter(REVISION_BY_HASH_PARAMETER, revisionHash);
        return getSingleResultOrNull(query);
    }

    @Override
    public RevisionEntity findById(final Long aLong) {
        final RevisionEntity revisionEntity = super.findById(aLong);
        if (revisionEntity != null) {
            Hibernate.initialize(revisionEntity.getMessages());
        }
        return revisionEntity;
    }
}
