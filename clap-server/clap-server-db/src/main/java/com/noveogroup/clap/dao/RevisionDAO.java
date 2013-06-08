package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.request.revision.StreamedPackage;


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
}
