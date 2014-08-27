package com.noveogroup.clap.rest.auth;

import com.noveogroup.clap.model.auth.ApkAuthentication;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.rest.AuthenticationEndpoint;
import com.noveogroup.clap.service.user.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class AuthenticationEndpointImpl implements AuthenticationEndpoint {

    @Inject
    private UserService userService;

    @Override
    public String getToken(final Authentication authentication) {
        final String token = userService.getToken(authentication);
        return token;
    }

    @Override
    public String getToken(final ApkAuthentication authentication) {
        final String token = userService.getApkToken(authentication);
        return token;
    }
}
