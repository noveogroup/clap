package com.noveogroup.clap.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

/**
 * @author Andrey Sokolov
 */
public class DelegatingExceptionHandler extends ExceptionHandlerWrapper{

    private final ExceptionHandler wrapped;
    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingExceptionHandler.class);

    public DelegatingExceptionHandler(final ExceptionHandler wrapped,
                                      final AbstractViewExpiredExceptionHandlerDelegate viewExpiredDelegate,
                                      final ExceptionHandlerDelegate... delegates) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }


    @Override
    public void handle() throws FacesException {
        Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();

            Throwable throwable = context.getException();

            FacesContext fc = FacesContext.getCurrentInstance();
            Throwable rootCause = getRootCause(throwable);
            try {
                //TODO  fix it! fix it! fix it!
                Flash flash = fc.getExternalContext().getFlash();

                // Put the exception in the flash scope to be displayed in the error
                // page if necessary ...
                flash.put("errorDetails", throwable.getMessage());

                LOGGER.error("the error is put in the flash: " + throwable.getMessage());

                NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();

                navigationHandler.handleNavigation(fc, null, "error?faces-redirect=true");

                fc.renderResponse();
            } finally {
                iterator.remove();
            }
        }

        // Let the parent handle the rest
        getWrapped().handle();
    }
}
