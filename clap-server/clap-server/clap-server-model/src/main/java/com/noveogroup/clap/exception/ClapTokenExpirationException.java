package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;

/**
 * @author Andrey Sokolov
 */
@ApplicationException
public class ClapTokenExpirationException extends ClapAuthenticationException {
    public ClapTokenExpirationException(final Throwable cause) {
        super(cause);
    }

    public ClapTokenExpirationException(final String message) {
        super(message);
    }

    public ClapTokenExpirationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
