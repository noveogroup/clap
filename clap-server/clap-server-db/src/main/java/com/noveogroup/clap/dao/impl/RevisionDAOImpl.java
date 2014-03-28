package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.revision.StreamedPackage;
import com.noveogroup.clap.model.revision.RevisionType;
import org.hibernate.Hibernate;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.engine.jdbc.LobCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class RevisionDAOImpl extends GenericHibernateDAOImpl<RevisionEntity, Long> implements RevisionDAO {

    private static final String REVISION_BY_HASH = "getRevisionByHash";
    private static final String REVISION_BY_HASH_PARAMETER = "hash";
    private static final String REVISIONS_BY_PROJECT_AND_TYPE = "getRevisionsByProjectAndType";
    private static final String REVISIONS_BY_PROJECT_AND_TYPE_PARAMETER_ID = "id";
    private static final String REVISIONS_BY_PROJECT_AND_TYPE_PARAMETER_TYPE = "type";
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
    public RevisionEntity persist(final RevisionEntity entity,
                                  final StreamedPackage mainPackage,
                                  final StreamedPackage specialPackage) {
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
        super.persist(entity);
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
    public List<RevisionEntity> findForProjectAndType(final Long projectId, final RevisionType type) {
        final Query query = entityManager.createNamedQuery(REVISIONS_BY_PROJECT_AND_TYPE);
        query.setParameter(REVISIONS_BY_PROJECT_AND_TYPE_PARAMETER_ID, projectId);
        query.setParameter(REVISIONS_BY_PROJECT_AND_TYPE_PARAMETER_TYPE,type);
        final List<RevisionEntity> resultList = query.getResultList();
        return resultList;
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
