package com.noveogroup.clap.integration.web.exception;

import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.web.exception.ExceptionHandlerDelegate;
import com.noveogroup.clap.web.util.message.MessageUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class AuthFailedExceptionHandlerDelegate implements ExceptionHandlerDelegate<ClapAuthenticationFailedException> {
    @Override
    public boolean handle(final FacesContext context, final ClapAuthenticationFailedException e) throws IOException {
        // managed by request helper for common cases
        // following will work for reset password feature
        final FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        message.setSummary(MessageUtils.getMessage(context, "user.info.error.password.incorrect"));
        context.addMessage(null, message);
        return true;
    }

    @Override
    public Class<ClapAuthenticationFailedException> getExceptionClass() {
        return ClapAuthenticationFailedException.class;
    }
}
