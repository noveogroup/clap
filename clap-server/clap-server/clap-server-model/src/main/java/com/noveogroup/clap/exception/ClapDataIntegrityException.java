package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException(rollback = true)
public class ClapDataIntegrityException extends ClapException {
    public ClapDataIntegrityException() {
    }

    public ClapDataIntegrityException(final String message) {
        super(message);
    }

    public ClapDataIntegrityException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClapDataIntegrityException(final Throwable cause) {
        super(cause);
    }
}
