package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.revision.CreateRevisionVariantRequest;
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
    public ClapResponse createOrUpdateRevision(final CreateRevisionVariantRequest request) {
        login(request.getToken());
        final boolean result = revisionService.createRevisionVariant(request);
        final ClapResponse response = new ClapResponse();
        response.setMessage(result ? "apk saved" : "apk not saved(apk with such hash already uploaded)");
        return response;

    }

    @Override
    public ClapResponse saveScreenshot(final ScreenshotMessageRequest request) {
        final String revisionHash = request.getVariantHash();
        LOGGER.debug("saving screenshot message " + revisionHash);
        login(request.getToken());
        messagesService.saveMessage(revisionHash, request.getMessage(), request.getScreenshotFileStream());
        LOGGER.debug(revisionHash + "screenshot saved");
        return new ClapResponse();
    }
}
