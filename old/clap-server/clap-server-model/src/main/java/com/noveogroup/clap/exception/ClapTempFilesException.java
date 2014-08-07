package com.noveogroup.clap.exception;

import javax.ejb.ApplicationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@ApplicationException(rollback = true)
public class ClapTempFilesException extends ClapException {
    //TODO get message
    private final List<IOException> exceptions;

    public ClapTempFilesException(final List<IOException> exceptions) {
        this.exceptions = exceptions;
    }

    public List<IOException> getExceptions() {
        return exceptions;
    }
}
