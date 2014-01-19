package com.noveogroup.clap.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class SomeViewExpiredExceptionHandlerDelegate implements ExceptionHandlerDelegate<ViewExpiredException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomeViewExpiredExceptionHandlerDelegate.class);

    @Override
    public boolean handle(final FacesContext context, final ViewExpiredException e) throws IOException {
        LOGGER.error("view expired");
        return false;
    }

    @Override
    public Class<ViewExpiredException> getExceptionClass() {
        return ViewExpiredException.class;
    }
}
