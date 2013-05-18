package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.entity.message.MessageEntity;

import javax.ejb.Stateless;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class MessageDAOImpl extends GenericHibernateDAOImpl<MessageEntity, Long> implements MessageDAO {
}
