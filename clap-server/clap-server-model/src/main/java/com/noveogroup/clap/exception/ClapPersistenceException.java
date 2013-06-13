package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException(rollback=true)
public class ClapPersistenceException extends ClapException {

    public ClapPersistenceException() {
    }

    public ClapPersistenceException(String message) {
        super(message);
    }

    public ClapPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClapPersistenceException(Throwable cause) {
        super(cause);
    }

    public ClapPersistenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
