package com.noveogroup.clap.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public class DelegatingExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingExceptionHandler.class);

    private final ExceptionHandler wrapped;

    public DelegatingExceptionHandler(final ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }


    @Override
    public void handle() throws FacesException {
        final Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            final ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            final Throwable throwable = context.getException();
            final FacesContext fc = FacesContext.getCurrentInstance();
            final ValueExpression valueExpression = fc.getApplication().getExpressionFactory()
                    .createValueExpression(fc.getELContext(), "#{delegatesInjector}", DelegatesInjector.class);
            final DelegatesInjector delegatesInjector = (DelegatesInjector) valueExpression.getValue(fc.getELContext());
            final Map<Class, ExceptionHandlerDelegate> delegatesMap = delegatesInjector.getDelegatesMap();
            try {
                if (delegateHandling(throwable, delegatesMap, fc)) {
                    iterator.remove();
                } else {
                    getWrapped().handle();
                }
            } catch (IOException e) {
                LOGGER.error("error while handling exception: " + throwable, e);
                getWrapped().handle();
            }
        }
    }

    private boolean delegateHandling(final Throwable e,
                                     final Map<Class, ExceptionHandlerDelegate> delegatesMap,
                                     final FacesContext context) throws IOException {
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof FacesException) {
                cause = cause.getCause();
                continue;
            }
            if (cause instanceof ELException) {
                cause = cause.getCause();
                continue;
            }
            if (cause instanceof EvaluationException) {
                cause = cause.getCause();
                continue;
            }
            Class exceptionClass = cause.getClass();
            while (!exceptionClass.equals(Throwable.class)) {
                final ExceptionHandlerDelegate delegate = delegatesMap.get(exceptionClass);
                if (delegate != null) {
                    if (delegate.handle(context, cause)) {
                        return true;
                    }
                }
                exceptionClass = exceptionClass.getSuperclass();
            }
            cause = cause.getCause();
        }
        LOGGER.debug("exception not handled: " + e);
        return false;
    }

}
