package com.noveogroup.clap.integration.web;

import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
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
    private AuthenticationSystemFactory authenticationSystemFactory;

    @Inject
    private AuthenticationRequestHelper authenticationRequestHelper;

    public String authentify(){
        authenticationSystemFactory.getAuthenticationSystem().authentifyUser(authenticationRequestHelper,null);
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
