package com.noveogroup.clap.exception;


import com.noveogroup.clap.model.auth.Authentication;

/**
 * @author Andrey Sokolov
 */
public class AuthenticationException extends ClapException {
    private final Authentication failedAuthentication;

    public AuthenticationException(Authentication failedAuthentication) {
        this.failedAuthentication = failedAuthentication;
    }

    public Authentication getFailedAuthentication() {
        return failedAuthentication;
    }
}
