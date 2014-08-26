package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.revision.RevisionVariantEntity;

/**
 * @author Andrey Sokolov
 */
public interface RevisionVariantDAO extends GenericDAO<RevisionVariantEntity, Long> {
    RevisionVariantEntity getRevisionByHash(String revisionHash);
    RevisionVariantEntity getRevisionByMessageId(long id);
}
