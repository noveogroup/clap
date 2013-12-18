package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class DefaultAuthenticationSystem implements AuthenticationSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationSystem.class);

    @Override
    public void authentifyUser(final AuthenticationRequestHelper authenticationHelper) {
        final RequestUserModel user = authenticationHelper.getUserRequestData();
        if (user != null) {
            LOGGER.debug("user : " + user);
            final UserWithPersistedAuth userPersistedData = authenticationHelper.getUserPersistedData();

            //TODO finish it, implement auth by authKey, hashing password and a lot of stuff....
            //TODO before finishing authentication not being checked
            if (!checkAuth(user, userPersistedData)) {
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
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("empty password");
        }
        try {
            final MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = password.getBytes();
            m.update(data, 0, data.length);
            final BigInteger i = new BigInteger(1, m.digest());
            final String calculatedHash = i.toString(16);
            return StringUtils.equals(persistedHash, calculatedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("impossibru!", e);
        }
    }

}
