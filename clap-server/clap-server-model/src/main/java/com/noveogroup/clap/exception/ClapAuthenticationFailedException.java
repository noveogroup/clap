package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException
public class ClapAuthenticationFailedException extends ClapException {
    public ClapAuthenticationFailedException() {
    }

    public ClapAuthenticationFailedException(final String message) {
        super(message);
    }

    public ClapAuthenticationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClapAuthenticationFailedException(final Throwable cause) {
        super(cause);
    }
}
