package com.noveogroup.clap.service.apk.exception;

/**
 * @author Andrey Sokolov
 */
public class ExtractingInfoException extends RuntimeException {
    public ExtractingInfoException() {
    }

    public ExtractingInfoException(final String message) {
        super(message);
    }

    public ExtractingInfoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExtractingInfoException(final Throwable cause) {
        super(cause);
    }
}
