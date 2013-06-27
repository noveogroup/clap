package com.noveogroup.clap.exception;

/**
 * @author Andrey Sokolov
 */
public class ClapAuthenticationFailedException extends ClapException {
    public ClapAuthenticationFailedException() {
    }

    public ClapAuthenticationFailedException(String message) {
        super(message);
    }

    public ClapAuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClapAuthenticationFailedException(Throwable cause) {
        super(cause);
    }

    public ClapAuthenticationFailedException(String message,
                                             Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
