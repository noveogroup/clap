package com.noveogroup.clap.rest.exception;


import com.noveogroup.clap.model.auth.Authentication;

/**
 * @author Andrey Sokolov
 */
public class AuthenticationException extends ClapException {
    private final Authentication failedAuthentication;

    public AuthenticationException(final Authentication failedAuthentication) {
        this.failedAuthentication = failedAuthentication;
    }

    public Authentication getFailedAuthentication() {
        return failedAuthentication;
    }
}
