package com.noveogroup.clap.rest;

import com.noveogroup.clap.rest.model.SendMessageRequest;
import com.noveogroup.clap.service.messages.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessagesEndpointImpl implements MessagesEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesEndpointImpl.class);

    @Inject
    private MessagesService messagesService;

    @Override
    public void saveMessage(SendMessageRequest request) {
        LOGGER.debug("saving message " + request);
        messagesService.saveMessage(request.getProjectName(),request.getRevisionTimestamp(),request.getMessageDTO());
        LOGGER.debug(request + "saved");
    }
}
