package com.noveogroup.clap.rest.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Andrey Sokolov
 */
public class ClapRestAuthenticationToken implements AuthenticationToken {

    private final String token;

    public ClapRestAuthenticationToken(final String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
