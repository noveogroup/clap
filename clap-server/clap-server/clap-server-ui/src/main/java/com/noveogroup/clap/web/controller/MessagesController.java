package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.model.revisions.RevisionVariantSessionModel;

import javax.enterprise.context.RequestScoped;
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

    public void prepareLogsView(){
        //TODO
    }

    public void prepareCrashView(){
        CrashMessage selectedCrashMessage = sessionModel.getSelectedCrashMessage();
        final String idString = getRequestParam("id");
        if(idString != null){
            final long id = Long.valueOf(idString);
            if(selectedCrashMessage == null || selectedCrashMessage.getId() != id){
                RevisionVariantWithApkStructure selectedRevisionVariant = sessionModel.getSelectedRevisionVariant();
                if(selectedRevisionVariant == null){
                    selectedRevisionVariant = revisionService.getRevisionVariantWithApkStructureByMessageId(id);
                } else {
                    selectedCrashMessage = findMessage(id, selectedRevisionVariant.getMessages());
                }
                if(selectedCrashMessage == null){
                    selectedRevisionVariant = revisionService.getRevisionVariantWithApkStructureByMessageId(id);
                    selectedCrashMessage = findMessage(id, selectedRevisionVariant.getMessages());
                }
                sessionModel.setSelectedCrashMessage(selectedCrashMessage);
            }
        }
    }

    private CrashMessage findMessage(final long id, final List<BaseMessage> messageList){
        for(BaseMessage message : messageList){
            if(message.getId() == id && message instanceof CrashMessage){
                return (CrashMessage) message;
            }
        }
        return null;
    }
}
