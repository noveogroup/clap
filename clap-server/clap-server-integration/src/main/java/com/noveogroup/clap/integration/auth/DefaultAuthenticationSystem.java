package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.auth.PasswordsHashCalculator;
import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
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
        final RequestUserModel user = authenticationHelper.getUserRequestData();
        if (user != null && StringUtils.isNotEmpty(user.getLogin())) {
            LOGGER.debug("user : " + user);
            final UserWithPersistedAuth userPersistedData = authenticationHelper.getUserPersistedData();
            if (checkAuth(user, userPersistedData)) {
                authenticationHelper.onLoginSuccessfull();
            } else {
                authenticationHelper.onLoginFailed();
            }
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

    protected boolean checkAuth(RequestUserModel requestUer, UserWithPersistedAuth persistedUser) {
        final String password = requestUer.getPassword();
        final String persistedHash = persistedUser.getAuthenticationKey();
        return StringUtils.equals(persistedHash, PasswordsHashCalculator.calculatePasswordHash(password));
    }
}
