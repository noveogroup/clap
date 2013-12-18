package com.noveogroup.clap.web.exception;

import com.noveogroup.clap.web.util.message.MessageUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class DefaultExceptionHandlerDelegate implements ExceptionHandlerDelegate<Exception> {
    @Override
    public boolean handle(FacesContext context, Exception e) throws IOException {
        final FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        message.setSummary(MessageUtils.getMessage(context, "error.unexpected", e.getMessage()));
        context.addMessage(null, message);
        return true;
    }

    @Override
    public Class<Exception> getExceptionClass() {
        return Exception.class;
    }
}
