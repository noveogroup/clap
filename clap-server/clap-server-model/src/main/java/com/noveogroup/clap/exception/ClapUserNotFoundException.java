package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException(rollback = false)
public class ClapUserNotFoundException extends ClapException {

    public ClapUserNotFoundException() {
    }

    public ClapUserNotFoundException(String message) {
        super(message);
    }

    public ClapUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClapUserNotFoundException(Throwable cause) {
        super(cause);
    }
}
