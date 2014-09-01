package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.model.revision.RevisionType;

/**
 * @author Mikhail Demidov
 */
public interface MessageDAO extends GenericDAO<BaseMessageEntity, Long> {

    <T extends BaseMessageEntity> int deleteMessages(long minTimestamp,
                                                     RevisionType revisionType);
}
