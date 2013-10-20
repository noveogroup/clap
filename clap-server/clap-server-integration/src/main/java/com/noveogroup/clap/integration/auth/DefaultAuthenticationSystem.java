package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithAuthentication;
import org.apache.commons.lang3.StringUtils;
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
        final UserWithAuthentication user = authenticationHelper.getUserRequestData();
        if (user != null) {
            LOGGER.debug("user : " + user);
            final User userPersistedData = authenticationHelper.getUserPersistedData();

            //TODO finish it, implement auth by authKey, hashing password and a lot of stuff....
            //TODO before finishing authentication not being checked
            if(userPersistedData != null){
                if (!StringUtils.equals(userPersistedData.getPassword(), user.getPassword())) {
                    throw new ClapAuthenticationFailedException();
                }
            }
            authenticationHelper.onLoginFailed();
        } else {
            LOGGER.debug(" user request data == null");
            authenticationHelper.onLoginRequired();
            throw new ClapAuthenticationFailedException();
        }
    }

    @Override
    public String getSystemId() {
        return AuthenticationSystemFactory.DEFAULT_SYSTEM_ID;
    }

}
