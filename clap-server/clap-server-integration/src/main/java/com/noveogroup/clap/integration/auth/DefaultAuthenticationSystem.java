package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class DefaultAuthenticationSystem implements AuthenticationSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationSystem.class);

    @Override
    public void authentifyUser(final AuthenticationRequestHelper authenticationHelper) {
        final User user = authenticationHelper.getUserData();
        if(user != null){
            LOGGER.debug("user : " + user);
        } else {
            LOGGER.debug(" user == null");
            //authenticationHelper.onLoginRequired();
        }
    }

    @Override
    public String getSystemId() {
        return AuthenticationSystemFactory.DEFAULT_SYSTEM_ID;
    }

}
