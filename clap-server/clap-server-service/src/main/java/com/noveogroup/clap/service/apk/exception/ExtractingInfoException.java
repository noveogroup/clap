package com.noveogroup.clap.service.apk.exception;

/**
 * @author Andrey Sokolov
 */
public class ExtractingInfoException extends RuntimeException {
    public ExtractingInfoException() {
    }

    public ExtractingInfoException(String message) {
        super(message);
    }

    public ExtractingInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractingInfoException(Throwable cause) {
        super(cause);
    }

    public ExtractingInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
