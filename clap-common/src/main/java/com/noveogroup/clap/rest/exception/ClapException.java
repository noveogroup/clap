package com.noveogroup.clap.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * base class for common business exceptions
 *
 * may be thrown via rest
 *
 * @author Andrey Sokolov
 */
public class ClapException extends WebApplicationException {
    public ClapException() {
    }

    public ClapException(final Response response) {
        super(response);
    }

    public ClapException(final int status) {
        super(status);
    }

    public ClapException(final Response.Status status) {
        super(status);
    }

    public ClapException(final Throwable cause) {
        super(cause);
    }

    public ClapException(final Throwable cause, final Response response) {
        super(cause, response);
    }

    public ClapException(final Throwable cause, final int status) {
        super(cause, status);
    }

    public ClapException(final Throwable cause, final Response.Status status) throws IllegalArgumentException {
        super(cause, status);
    }
}
