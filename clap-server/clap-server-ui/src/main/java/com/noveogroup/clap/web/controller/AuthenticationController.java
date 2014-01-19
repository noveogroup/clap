package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.auth.AuthenticationSystem;
import com.noveogroup.clap.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.auth.FromFactory;
import com.noveogroup.clap.integration.RequestHelperFactory;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.UserSessionData;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class AuthenticationController {

    @Inject
    private UserSessionData userSessionData;

    @Inject
    @FromFactory
    private AuthenticationSystem authenticationSystem;

    @Inject
    private AuthenticationRequestHelper authenticationRequestHelper;

    @Inject
    private RequestHelperFactory requestHelperFactory;

    public String authentify(){
        authenticationSystem.authentifyUser(authenticationRequestHelper, null);
        final String requestedView = userSessionData.getRequestedView();
        if(StringUtils.isNotEmpty(requestedView)){
            return requestedView;
        } else {
            return Navigation.HOME.getView();
        }
    }

    public String logout(){
        userSessionData.reset();
        return Navigation.HOME.getView();
    }
}
