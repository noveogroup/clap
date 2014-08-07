package com.noveogroup.clap.exception;

/**
 * @author Andrey Sokolov
 */
public abstract class ClapException extends RuntimeException {

    protected ClapException() {
    }

    protected ClapException(String message) {
        super(message);
    }

    protected ClapException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ClapException(Throwable cause) {
        super(cause);
    }
}
