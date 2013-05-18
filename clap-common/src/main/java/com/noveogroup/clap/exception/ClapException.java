package com.noveogroup.clap.exception;

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

    public ClapException(Response response) {
        super(response);
    }

    public ClapException(int status) {
        super(status);
    }

    public ClapException(Response.Status status) {
        super(status);
    }

    public ClapException(Throwable cause) {
        super(cause);
    }

    public ClapException(Throwable cause, Response response) {
        super(cause, response);
    }

    public ClapException(Throwable cause, int status) {
        super(cause, status);
    }

    public ClapException(Throwable cause, Response.Status status) throws IllegalArgumentException {
        super(cause, status);
    }
}
