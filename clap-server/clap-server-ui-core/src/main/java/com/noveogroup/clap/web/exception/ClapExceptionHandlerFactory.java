package com.noveogroup.clap.web.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * @author Andrey Sokolov
 */
public class ClapExceptionHandlerFactory extends ExceptionHandlerFactory {


    private final ExceptionHandlerFactory parent;

    public ClapExceptionHandlerFactory(final ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new DelegatingExceptionHandler(parent.getExceptionHandler());
    }
}
