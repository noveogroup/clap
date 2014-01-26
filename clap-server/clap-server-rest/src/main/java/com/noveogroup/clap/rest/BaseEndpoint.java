package com.noveogroup.clap.rest;

import com.noveogroup.clap.rest.auth.ClapRestAuthenticationToken;
import org.apache.shiro.SecurityUtils;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseEndpoint {

    protected void login(final String token) {
        SecurityUtils.getSubject().login(new ClapRestAuthenticationToken(token));
    }
}
