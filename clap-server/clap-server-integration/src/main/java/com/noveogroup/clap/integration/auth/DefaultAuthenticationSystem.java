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
    public boolean authentifyUser(final AuthenticationRequestHelper authenticationHelper) {
        final User user = authenticationHelper.getUserRequestData();
        if(user != null){
            LOGGER.debug("user : " + user);
            final User userPersistedData = authenticationHelper.getUserPersistedData();

            //TODO finish it, implement auth by authKey, hashing password and a lot of stuff....
            if(user.getPassword() != userPersistedData.getPassword()){
                authenticationHelper.onLoginFailed();
                return false;
            }else {
                return true;
            }
        } else {
            LOGGER.debug(" user == null");
            authenticationHelper.onLoginRequired();
            return false;
        }
    }

    @Override
    public String getSystemId() {
        return AuthenticationSystemFactory.DEFAULT_SYSTEM_ID;
    }

}
