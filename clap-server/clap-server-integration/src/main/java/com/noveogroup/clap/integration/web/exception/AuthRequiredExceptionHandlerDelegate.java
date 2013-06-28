package com.noveogroup.clap.integration.web.exception;

import com.noveogroup.clap.exception.ClapAuthenticationRequiredException;
import com.noveogroup.clap.web.exception.ExceptionHandlerDelegate;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class AuthRequiredExceptionHandlerDelegate
        implements ExceptionHandlerDelegate<ClapAuthenticationRequiredException> {
    @Override
    public boolean handle(final FacesContext context,
                          final ClapAuthenticationRequiredException e) throws IOException {
        // managed by request helper
        return true;
    }

    @Override
    public Class<ClapAuthenticationRequiredException> getExceptionClass() {
        return ClapAuthenticationRequiredException.class;
    }
}
