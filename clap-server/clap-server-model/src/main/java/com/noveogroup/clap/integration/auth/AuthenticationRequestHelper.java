package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationRequestHelper extends RequestHelper {
    Authentication getUserRequestData();
    UserWithPersistedAuth getUserPersistedData();
    void applyAuthentication(Authentication authentication);
    void onLoginSuccessfull(final User user);
    void onLoginRequired();
    void onLoginFailed();
    void onConstraintViolate(final List<ConstraintViolation> constraintViolationList);
}
