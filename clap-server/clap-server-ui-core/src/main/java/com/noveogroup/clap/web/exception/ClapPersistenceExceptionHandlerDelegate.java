package com.noveogroup.clap.web.exception;

import com.noveogroup.clap.exception.ClapPersistenceException;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class ClapPersistenceExceptionHandlerDelegate implements ExceptionHandlerDelegate<ClapPersistenceException> {

    @Override
    public boolean handle(final FacesContext context, final ClapPersistenceException e) throws IOException {
        final FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        message.setSummary(e.getMessage());
        context.addMessage(null, message);
        return true;
    }

    @Override
    public Class<ClapPersistenceException> getExceptionClass() {
        return ClapPersistenceException.class;
    }
}
