package com.noveogroup.clap.web.exception;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
public class ClapExceptionHandlerFactory extends ExceptionHandlerFactory {


    private ExceptionHandlerFactory parent;

    public ClapExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new DelegatingExceptionHandler(parent.getExceptionHandler(),
                new AbstractViewExpiredExceptionHandlerDelegate() {
            @Override
            public boolean handle(FacesContext context, ViewExpiredException e) throws IOException {
                return false;
            }
        });
    }
}
