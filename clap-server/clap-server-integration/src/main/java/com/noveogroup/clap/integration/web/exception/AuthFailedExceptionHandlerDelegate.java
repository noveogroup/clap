package com.noveogroup.clap.integration.web.exception;

import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.web.exception.ExceptionHandlerDelegate;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class AuthFailedExceptionHandlerDelegate implements ExceptionHandlerDelegate<ClapAuthenticationFailedException> {
    @Override
    public boolean handle(final FacesContext context, final ClapAuthenticationFailedException e) throws IOException {
        // managed by request helper
        return true;
    }

    @Override
    public Class<ClapAuthenticationFailedException> getExceptionClass() {
        return ClapAuthenticationFailedException.class;
    }
}
