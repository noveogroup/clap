package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.revision.Revision;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class RevisionDAOImpl extends GenericHibernateDAOImpl<Revision, Long> implements RevisionDAO {

    private static final String REVISION_BY_TIMESTAMP = "getRevisionByTimestamp";
    private static final String REVISION_BY_TIMESTAMP_PARAMETER = "timestamp";

    @Override
    public Revision getRevisionByTimestamp(Long timestamp) {
        Query query = entityManager.createNamedQuery(REVISION_BY_TIMESTAMP);
        query.setParameter(REVISION_BY_TIMESTAMP_PARAMETER,timestamp);
        return (Revision) query.getSingleResult();
    }
}
