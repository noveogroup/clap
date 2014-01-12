package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.UserCreationModel;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.RegisterUserModel;
import com.noveogroup.clap.web.model.user.UserSessionData;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class RegisterUserController {

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private RegisterUserModel registerUserModel;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationSystemFactory authenticationSystemFactory;

    @Inject
    private AuthenticationRequestHelper authenticationRequestHelper;


    public String registerUser(){
        final UserCreationModel user = registerUserModel.getUser();
        userService.createUser(user);
        final Authentication authentication = userSessionData.getAuthentication();
        authentication.setLogin(user.getLogin());
        authentication.setPassword(user.getPassword());
        authenticationSystemFactory.getAuthenticationSystem().authentifyUser(authenticationRequestHelper,null);
        return Navigation.HOME.getView();
    }

}
