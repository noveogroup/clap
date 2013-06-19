package com.noveogroup.clap.web.exception;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
public interface ExceptionHandlerDelegate<T extends Throwable> {

    /**
     * @param context context of thrown exception
     * @param e       delegated exception
     * @return true if exception should be removed from queue
     * @throws IOException can be thrown at redirect
     */
    boolean handle(FacesContext context, T e) throws IOException;

    Class<T> getExceptionClass();

}
