package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.entity.message.BaseMessageEntity;

import javax.ejb.Stateless;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class MessageDAOImpl extends GenericHibernateDAOImpl<BaseMessageEntity, Long> implements MessageDAO {
}
