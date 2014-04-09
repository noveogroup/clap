package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
import com.noveogroup.clap.service.messages.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessagesEndpointImpl extends BaseEndpoint implements MessagesEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesEndpointImpl.class);

    @Inject
    private MessagesService messagesService;

    @Override
    public void saveCrashMessage(final SendMessageRequest request) {
        LOGGER.debug("saving crash message " + request);
        login(request.getToken());
        messagesService.saveMessage(request.getRevisionHash(), request.getMessage());
        LOGGER.debug(request + "saved");
    }

    @Override
    public void saveScreenshot(final ScreenshotMessageRequest request) {
        LOGGER.debug("saving screenshot message " + request);
        login(request.getToken());
        //TODO
        LOGGER.debug(request + "saved");
    }
}
