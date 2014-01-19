package com.noveogroup.clap.auth;

import com.google.common.collect.Lists;
import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class DefaultAuthenticationSystem implements AuthenticationSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationSystem.class);

    @Override
    public void authentifyUser(final AuthenticationRequestHelper authenticationHelper,
                               final List<AuthenticationConstraint> constraints) {
        final Authentication user = authenticationHelper.getUserRequestData();
        if (user != null && StringUtils.isNotEmpty(user.getLogin())) {
            LOGGER.debug("user : " + user);
            final UserWithPersistedAuth userPersistedData = authenticationHelper.getUserPersistedData();
            if (checkAuth(user, userPersistedData)) {
                final List<ConstraintViolation> violations = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(constraints)) {
                    for (AuthenticationConstraint constraint : constraints) {
                        final ConstraintViolation violation = constraint.isSatisfy(userPersistedData);
                        if (violation != null) {
                            violations.add(violation);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(violations)) {
                    authenticationHelper.onConstraintViolate(violations);
                } else {
                    authenticationHelper.onLoginSuccessfull(userPersistedData);
                }
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

    protected boolean checkAuth(Authentication requestUer, UserWithPersistedAuth persistedUser) {
        final String password = requestUer.getPassword();
        final String persistedHash = persistedUser.getAuthenticationKey();
        return StringUtils.equals(persistedHash, PasswordsHashCalculator.calculatePasswordHash(password));
    }
}
