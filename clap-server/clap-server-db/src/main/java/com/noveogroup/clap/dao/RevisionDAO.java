package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.revision.RevisionEntity;

/**
 * @author Mikhail Demidov
 */
public interface RevisionDAO extends GenericDAO<RevisionEntity, Long> {
    RevisionEntity getRevisionByHash(String revisionHash);
}
