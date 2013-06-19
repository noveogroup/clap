package com.noveogroup.clap.web.exception;

import javax.faces.application.ViewExpiredException;

/**
 * @author Andrey Sokolov
 */
public abstract class AbstractViewExpiredExceptionHandlerDelegate
        implements ExceptionHandlerDelegate<ViewExpiredException>{

    @Override
    public Class<ViewExpiredException> getExceptionClass() {
        return ViewExpiredException.class;
    }
}
