package com.noveogroup.clap.rest;

import com.noveogroup.clap.facade.MessagesFacade;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessagesEndpointImpl implements MessagesEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesEndpointImpl.class);

    @Inject
    private MessagesFacade messagesFacade;

    @Override
    public void saveMessage(final SendMessageRequest request) {
        LOGGER.debug("saving message " + request);
        messagesFacade.saveMessage(request.getRevisionHash(), request.getMessage());
        LOGGER.debug(request + "saved");
    }
}
