package com.noveogroup.clap.exception;

/**
 * @author Andrey Sokolov
 */
public abstract class ClapException extends RuntimeException {

    public ClapException() {
    }

    public ClapException(final String message) {
        super(message);
    }

    public ClapException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClapException(final Throwable cause) {
        super(cause);
    }

    public ClapException(final String message,
                         final Throwable cause,
                         final boolean enableSuppression,
                         final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
