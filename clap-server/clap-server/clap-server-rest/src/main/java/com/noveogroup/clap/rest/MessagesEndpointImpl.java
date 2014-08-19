package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.message.CrashMessageRequest;
import com.noveogroup.clap.model.request.message.LogsBunchMessageRequest;
import com.noveogroup.clap.service.messages.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.File;

@ApplicationScoped
public class MessagesEndpointImpl extends BaseEndpoint implements MessagesEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesEndpointImpl.class);

    @Inject
    private MessagesService messagesService;

    @Override
    public void saveCrashMessage(final CrashMessageRequest request) {
        LOGGER.debug("saving crash message " + request);
        login(request.getToken());
        messagesService.saveMessage(request.getRevisionHash(), request.getMessage());
        LOGGER.debug(request + "saved");
    }

    @Override
    public void saveLogsBunchMessage(final LogsBunchMessageRequest request) {
        LOGGER.debug("saving logs bunch message " + request);
        login(request.getToken());
        messagesService.saveMessage(request.getRevisionHash(), request.getMessage());
        LOGGER.debug(request + "saved");
    }


    public Response getScreenshot(final long messageId) {
        final File screenshot = messagesService.getScreenshot(messageId);
        if (screenshot != null) {
            return Response.ok(screenshot).header("Content-Disposition",
                    "attachment; filename=\"" + screenshot.getName() + "\"").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
