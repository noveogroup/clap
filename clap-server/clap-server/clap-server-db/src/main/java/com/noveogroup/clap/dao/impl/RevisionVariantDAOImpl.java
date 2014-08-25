package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionVariantDAO;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
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
        final RevisionVariantEntity revisionEntity = getSingleResultOrNull(query);
        initialize(revisionEntity);
        return revisionEntity;
    }

    private void initialize(final RevisionVariantEntity revisionEntity) {
        if (revisionEntity != null) {
            for(BaseMessageEntity message : revisionEntity.getMessages()){
                Hibernate.initialize(message.getDeviceInfo());
            }
        }
    }

    @Override
    public RevisionVariantEntity findById(final Long aLong) {
        final RevisionVariantEntity entity = super.findById(aLong);
        initialize(entity);
        return entity;
    }
}
