package com.noveogroup.clap.facade;

import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.message.Message;
import com.noveogroup.clap.service.messages.MessagesService;
import com.noveogroup.clap.transaction.Transactional;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({ClapMainInterceptor.class})
public class MessagesFacade {

    @Inject
    private MessagesService messagesService;

    @Transactional
    public void saveMessage(final String revisionHash, final Message message) {
        messagesService.saveMessage(revisionHash, message);
    }
}
