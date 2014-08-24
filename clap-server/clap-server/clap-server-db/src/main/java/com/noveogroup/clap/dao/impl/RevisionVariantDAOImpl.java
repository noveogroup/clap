package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionVariantDAO;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class RevisionVariantDAOImpl extends GenericDAOImpl<RevisionVariantEntity,Long> implements RevisionVariantDAO {
    private static final String REVISION_BY_HASH = "getRevisionVariantByHash";
    private static final String REVISION_BY_HASH_PARAMETER = "hash";


    @Override
    public RevisionVariantEntity getRevisionByHash(final String revisionHash) {
        final Query query = entityManager.createNamedQuery(REVISION_BY_HASH);
        query.setParameter(REVISION_BY_HASH_PARAMETER, revisionHash);
        final RevisionVariantEntity revisionEntity = (RevisionVariantEntity) query.getSingleResult();
        if (revisionEntity != null) {
            Hibernate.initialize(revisionEntity.getMessages());
        }
        return revisionEntity;
    }

    @Override
    public RevisionVariantEntity findById(final Long aLong) {
        final RevisionVariantEntity entity = super.findById(aLong);
        if(entity != null){
            Hibernate.initialize(entity.getMessages());
        }
        return entity;
    }
}
