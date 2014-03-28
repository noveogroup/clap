package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.revision.StreamedPackage;
import com.noveogroup.clap.model.revision.RevisionType;

import java.util.List;


/**
 * @author Mikhail Demidov
 */
public interface RevisionDAO extends GenericDAO<RevisionEntity, Long> {
    RevisionEntity getRevisionByHash(String revisionHash);

    RevisionEntity getRevisionByHashOrNull(String revisionHash);

    /**
     * streams will be closed after persisting
     *
     * @param entity
     * @param mainPackage
     * @param specialPackage
     * @return persisted entity
     */
    RevisionEntity persist(RevisionEntity entity,
                           StreamedPackage mainPackage,
                           StreamedPackage specialPackage);

    List<RevisionEntity> findForProjectAndType(Long projectId, RevisionType type);
}
