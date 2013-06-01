package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.message.Message;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MessagesServiceImpl implements MessagesService {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @Override
    public void saveMessage(final String revisionHash, final Message message) {
        final RevisionEntity revisionEntity = revisionDAO.getRevisionByHash(revisionHash);
        MessageEntity messageEntity = MAPPER.map(message, MessageEntity.class);
        messageEntity = messageDAO.persist(messageEntity);
        List<MessageEntity> messageEntities = revisionEntity.getMessages();
        if (messageEntities == null) {
            messageEntities = new ArrayList<MessageEntity>();
            revisionEntity.setMessages(messageEntities);
        }
        messageEntities.add(messageEntity);
        revisionDAO.persist(revisionEntity);
    }
}
