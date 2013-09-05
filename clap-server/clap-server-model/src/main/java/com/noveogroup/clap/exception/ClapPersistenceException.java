package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException(rollback=true)
public class ClapPersistenceException extends ClapException {

    public ClapPersistenceException() {
    }

    public ClapPersistenceException(final String message) {
        super(message);
    }

    public ClapPersistenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClapPersistenceException(final Throwable cause) {
        super(cause);
    }
}
