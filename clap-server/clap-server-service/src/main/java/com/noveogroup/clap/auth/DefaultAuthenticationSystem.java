package com.noveogroup.clap.auth;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class DefaultAuthenticationSystem implements AuthenticationSystem {

    public static final String SYSTEM_ID = "DEFAULT";

    @Override
    public User authentifyUser(Authentication authentication) {
        //TODO
        return null;
    }

    @Override
    public String getSystemId() {
        return SYSTEM_ID;
    }
}
