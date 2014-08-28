package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.model.revisions.RevisionVariantSessionModel;
import com.noveogroup.clap.web.util.message.MessageSupport;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class MessagesController extends BaseController {

    @Inject
    private RevisionVariantSessionModel sessionModel;
    @Inject
    private RevisionService revisionService;

    @Inject
    private MessageSupport messageSupport;

    public void prepareLogsView() {
        final LogsBunchMessage selectedLogsMessage = sessionModel.getSelectedLogsMessage();
        final LogsBunchMessage loadedMessage = loadMessage(selectedLogsMessage, LogsBunchMessage.class);
        sessionModel.setSelectedLogsMessage(loadedMessage);
    }

    public void prepareCrashView() {
        CrashMessage selectedCrashMessage = sessionModel.getSelectedCrashMessage();
        final CrashMessage loadedMessage = loadMessage(selectedCrashMessage, CrashMessage.class);
        sessionModel.setSelectedCrashMessage(loadedMessage);
    }

    private <T extends BaseMessage> T loadMessage(T selectedMessage, final Class<T> messageClass) {
        final String idString = getRequestParam("id");
        if (idString != null) {
            final long id = Long.valueOf(idString);
            if (selectedMessage == null || selectedMessage.getId() != id) {
                RevisionVariantWithApkStructure selectedRevisionVariant = sessionModel.getSelectedRevisionVariant();
                if (selectedRevisionVariant == null) {
                    selectedRevisionVariant = revisionService.getRevisionVariantWithApkStructureByMessageId(id);
                } else {
                    selectedMessage = findMessage(id, selectedRevisionVariant.getMessages(), messageClass);
                }
                if (selectedMessage == null) {
                    selectedRevisionVariant = revisionService.getRevisionVariantWithApkStructureByMessageId(id);
                    if (selectedRevisionVariant != null) {
                        selectedMessage = findMessage(id, selectedRevisionVariant.getMessages(), messageClass);
                    }
                }
                sessionModel.setSelectedRevisionVariant(selectedRevisionVariant);
                if (selectedMessage == null) {
                    final FacesMessage message = new FacesMessage(messageSupport.getMessage("error.badRequest.message",
                            new Object[]{id}));
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    messageSupport.addMessage(null,
                            message);
                }
            }
            return selectedMessage;
        } else {
            messageSupport.addMessage("error.badRequest.noId");
        }
        return null;
    }

    private <T extends BaseMessage> T findMessage(final long id,
                                                  final List<BaseMessage> messageList,
                                                  final Class<T> messageClass) {
        for (BaseMessage message : messageList) {
            if (message.getId() == id && messageClass.isAssignableFrom(message.getClass())) {
                return (T) message;
            }
        }
        return null;
    }
}
