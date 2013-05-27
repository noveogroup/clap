package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.web.model.UserSessionData;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class AuthenticationController {

    private static final String FORM_ID_LOGIN = "loginForm:login";
    private static final String FORM_ID_PASSWORD = "loginForm:password";

    @Inject
    private UserSessionData userSessionData;

    public String authentify(){
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String,String> requestParametersMap = context.getExternalContext().getRequestParameterMap();
        final String login = requestParametersMap.get(FORM_ID_LOGIN);
        final String password = requestParametersMap.get(FORM_ID_PASSWORD);
        final Authentication authentication = new Authentication();
        final User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        authentication.setUser(user);
        userSessionData.setUser(user);
        return userSessionData.getRequestedView()+"?faces-redirect=true";
    }
}
