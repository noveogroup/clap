package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException
public class ClapAuthenticationRequiredException extends ClapAuthenticationException {

    public ClapAuthenticationRequiredException(final String message) {
        super(message);
    }

    public ClapAuthenticationRequiredException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClapAuthenticationRequiredException(final Throwable cause) {
        super(cause);
    }
}
