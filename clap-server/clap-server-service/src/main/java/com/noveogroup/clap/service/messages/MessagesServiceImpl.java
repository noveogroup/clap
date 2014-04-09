package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.converter.MessagesConverter;
import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.service.user.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MessagesServiceImpl implements MessagesService {


    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserService userService;

    private MessagesConverter messagesConverter = new MessagesConverter();

    @RequiresAuthentication
    @WrapException
    @Override
    public void saveMessage(final String revisionHash, final CrashMessage message) {
        final RevisionEntity revisionEntity = revisionDAO.getRevisionByHash(revisionHash);
        UserEntity userByLogin = userDAO.getUserByLogin(userService.getCurrentUserLogin());
        MessageEntity messageEntity = messagesConverter.map(message);
        messageEntity.setRevision(revisionEntity);
        messageEntity.setUploadedBy(userByLogin);
        messageEntity = messageDAO.persist(messageEntity);
        List<MessageEntity> messageEntities = revisionEntity.getMessages();
        if (messageEntities == null) {
            messageEntities = new ArrayList<MessageEntity>();
            revisionEntity.setMessages(messageEntities);
        }
        messageEntities.add(messageEntity);
        revisionDAO.persist(revisionEntity);
        revisionDAO.flush();
        messageDAO.flush();
    }

    public void setMessagesConverter(final MessagesConverter messagesConverter) {
        this.messagesConverter = messagesConverter;
    }
}
