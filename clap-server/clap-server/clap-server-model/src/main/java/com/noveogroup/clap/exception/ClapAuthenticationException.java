package com.noveogroup.clap.exception;

/**
 * @author Andrey Sokolov
 */
public class ClapAuthenticationException extends ClapException {


    public ClapAuthenticationException(final Throwable cause) {
        super(cause);
    }

    public ClapAuthenticationException(final String message) {
        super(message);
    }

    public ClapAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
