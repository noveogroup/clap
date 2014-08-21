package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.response.ClapResponse;
import com.noveogroup.clap.service.messages.MessagesService;
import com.noveogroup.clap.service.revision.RevisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class UploadFileEndpointImpl extends BaseEndpoint implements UploadFileEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileEndpointImpl.class);

    @Inject
    private MessagesService messagesService;

    @Inject
    private RevisionService revisionService;

    @Override
    public ClapResponse createOrUpdateRevision(final CreateOrUpdateRevisionRequest request) {
        login(request.getToken());
        final boolean result = revisionService.addOrGetRevision(request);
        final ClapResponse response = new ClapResponse();
        response.setMessage(result ? "saved" : "not saved");
        return response;

    }

    @Override
    public ClapResponse saveScreenshot(final ScreenshotMessageRequest request) {
        final String revisionHash = request.getRevisionHash();
        LOGGER.debug("saving screenshot message " + revisionHash);
        login(request.getToken());
        messagesService.saveMessage(revisionHash, new ScreenshotMessage(), request.getScreenshotFileStream());
        LOGGER.debug(revisionHash + "screenshot saved");
        return new ClapResponse();
    }
}
