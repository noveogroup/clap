package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.message.MessageDTO;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({TransactionInterceptor.class})
public class MessagesServiceImpl implements MessagesService {

    private static Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @Transactional
    @Override
    public void saveMessage(long revisionTimestamp, MessageDTO messageDTO) {
        RevisionEntity revisionEntity = revisionDAO.getRevisionByTimestamp(revisionTimestamp);
        MessageEntity messageEntity = MAPPER.map(messageDTO, MessageEntity.class);
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
