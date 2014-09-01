package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.message.LogsBunchMessage;
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
public class LogsBunchMessageConverter extends BaseModelConverter implements Converter {

    @Inject
    private MessagesService messagesService;


    @Override
    protected Object getObject(final Long id) {
        return messagesService.getMessage(id, LogsBunchMessage.class);
    }

    @Override
    protected String getErrorMessageId() {
        return "error.badRequest.message";
    }
}
