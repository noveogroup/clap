package com.noveogroup.clap.rest;

import com.noveogroup.clap.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.rest.exception.AuthConstraintViolationException;
import com.noveogroup.clap.rest.exception.AuthenticationException;
import com.noveogroup.clap.service.user.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class RestAuthenticationRequestHelper implements AuthenticationRequestHelper {

    @Inject
    private UserService userService;


    private Authentication authentication;


    @Override
    public Authentication getUserRequestData() {
        return authentication;
    }

    @Override
    public UserWithPersistedAuth getUserPersistedData() {
        if (authentication != null) {
            return userService.getUserWithPersistedAuth(authentication.getLogin());
        } else {
            throw new IllegalStateException("user request data can't be null");
        }
    }

    @Override
    public void applyAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public void onLoginSuccessfull(final User user) {
        //it is REST, just skip
    }

    @Override
    public void onLoginRequired() {
        //TODO check if it's being called
        throw new AuthenticationException(authentication);
    }

    @Override
    public void onLoginFailed() {
        throw new AuthenticationException(authentication);
    }

    @Override
    public void onConstraintViolate(List<ConstraintViolation> constraintViolationList) {
        throw new AuthConstraintViolationException(constraintViolationList);
    }

    @Override
    public Class<? extends RequestHelper> getType() {
        return AuthenticationRequestHelper.class;
    }
}
