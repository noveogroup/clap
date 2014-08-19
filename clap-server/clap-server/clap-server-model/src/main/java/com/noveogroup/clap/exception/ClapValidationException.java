package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException
public class ClapValidationException extends ClapException {

    public ClapValidationException() {
    }

    public ClapValidationException(String message) {
        super(message);
    }

    public ClapValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClapValidationException(Throwable cause) {
        super(cause);
    }
}
