package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.request.revision.StreamedPackage;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class RevisionDAOImpl extends GenericHibernateDAOImpl<RevisionEntity, Long> implements RevisionDAO {

    private static final String REVISION_BY_HASH = "getRevisionByHash";
    private static final String REVISION_BY_HASH_PARAMETER = "hash";
    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionDAOImpl.class);

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
    public RevisionEntity persist(RevisionEntity entity, StreamedPackage mainPackage, StreamedPackage specialPackage) {
        final HibernateEntityManager hibernateEntityManager = entityManager.unwrap(HibernateEntityManager.class);
        final LobCreator lobCreator = Hibernate.getLobCreator(hibernateEntityManager.getSession());
        if (mainPackage != null) {
            entity.setMainPackage(
                    lobCreator.createBlob(
                            mainPackage.getStream(),
                            mainPackage.getLength()));
        }
        if (specialPackage != null) {
            entity.setSpecialPackage(
                    lobCreator.createBlob(
                            specialPackage.getStream(),
                            specialPackage.getLength()));
        }
        entityManager.persist(entity);
        entityManager.flush();
        try {
            if (entity.getMainPackage() != null) {
                entity.getMainPackage().free();
            }
            if (entity.getSpecialPackage() != null) {
                entity.getSpecialPackage().free();
            }
        } catch (SQLException e) {
            LOGGER.error("freeing blobs error", e);
        }
        return entity;
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
