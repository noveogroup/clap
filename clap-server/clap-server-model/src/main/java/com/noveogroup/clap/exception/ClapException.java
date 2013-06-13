package com.noveogroup.clap.exception;

/**
 * @author Andrey Sokolov
 */
public abstract class ClapException extends Exception {

    public ClapException() {
    }

    public ClapException(String message) {
        super(message);
    }

    public ClapException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClapException(Throwable cause) {
        super(cause);
    }

    public ClapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
