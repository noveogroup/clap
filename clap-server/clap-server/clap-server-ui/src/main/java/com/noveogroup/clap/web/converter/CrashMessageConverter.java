package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.service.messages.MessagesService;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class CrashMessageConverter extends BaseModelConverter implements Converter {

    @Inject
    private MessagesService messagesService;


    @Override
    protected Object getObject(final Long id) {
        return messagesService.getMessage(id, CrashMessage.class);
    }

    @Override
    protected String getErrorMessageId() {
        return "error.badRequest.message";
    }
}
