package com.noveogroup.clap.service.messages;

import com.google.common.collect.Lists;
import com.noveogroup.clap.converter.MessagesConverter;
import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionVariantDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.message.ScreenshotMessageEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.exception.ClapDataIntegrityException;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.file.FileType;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.service.file.FileService;
import com.noveogroup.clap.service.user.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MessagesServiceImpl implements MessagesService {


    @EJB
    private RevisionVariantDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserService userService;

    @Inject
    private FileService fileService;

    private MessagesConverter messagesConverter = new MessagesConverter();

    @RequiresAuthentication
    @WrapException
    @Override
    public void saveMessage(final String variantHash, final BaseMessage message) {
        final RevisionVariantEntity revisionEntity = revisionDAO.getRevisionByHash(variantHash);
        if (revisionEntity != null) {
            BaseMessageEntity messageEntity = messagesConverter.map(message);
            messageEntity.setRevisionVariant(revisionEntity);
            messageEntity = messageDAO.persist(messageEntity);
            List<BaseMessageEntity> messageEntities = revisionEntity.getMessages();
            if (messageEntities == null) {
                messageEntities = Lists.newArrayList();
                revisionEntity.setMessages(messageEntities);
            }
            messageEntities.add(messageEntity);
            revisionDAO.persist(revisionEntity);
            revisionDAO.flush();
        } else {
            throw new ClapDataIntegrityException("no such variant stored, hash: " + variantHash);
        }
    }

    @RequiresAuthentication
    @WrapException
    @Override
    public void saveMessage(final String variantHash, final BaseMessage message, final InputStream inputStream) {
        if (inputStream != null) {
            if (message instanceof ScreenshotMessage) {
                ScreenshotMessage screenshotMessage = (ScreenshotMessage) message;
                final File file = fileService.saveFile(FileType.SCREENSHOT, inputStream, null, ".png");
                screenshotMessage.setScreenshotUrl(file.getAbsolutePath());
            }
        }
        saveMessage(variantHash, message);
    }

    @Override
    public File getScreenshot(final long messageId) {
        final BaseMessageEntity byId = messageDAO.findById(messageId);
        if (byId instanceof ScreenshotMessageEntity) {
            return fileService.getFile(((ScreenshotMessageEntity) byId).getScreenshotFileUrl());
        } else {
            return null;
        }
    }

    public void setMessagesConverter(final MessagesConverter messagesConverter) {
        this.messagesConverter = messagesConverter;
    }
}
