package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.model.revision.RevisionType;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class MessageDAOImpl extends GenericDAOImpl<BaseMessageEntity, Long> implements MessageDAO {

    public static final String MESSAGES_TO_DELETE = "SELECT m FROM BaseMessageEntity m " +
            "WHERE m.timestamp < :timestamp AND m.revisionVariant.revision.revisionType = :revisionType";

    @Override
    public <T extends BaseMessageEntity> int deleteMessages(final long minTimestamp,
                                                            final RevisionType revisionType) {
        final Query query = entityManager.createQuery(MESSAGES_TO_DELETE);
        query.setParameter("timestamp",new Date(minTimestamp));
        query.setParameter("revisionType",revisionType);
        final List<BaseMessageEntity> resultList = query.getResultList();
        if (resultList != null) {
            for (BaseMessageEntity messageEntity : resultList) {
                remove(messageEntity);
            }
            return resultList.size();
        }
        return 0;
    }
}
