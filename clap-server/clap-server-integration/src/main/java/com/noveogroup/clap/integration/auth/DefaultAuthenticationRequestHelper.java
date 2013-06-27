package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.rest.exception.AuthenticationException;
import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithAuthentication;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.UserSessionData;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class DefaultAuthenticationRequestHelper implements AuthenticationRequestHelper {

    @Inject
    private UserService userService;

    private UserWithAuthentication userRequestData;

    @Inject
    private UserSessionData userSessionData;

    //for JSF
    private FacesContext context;
    //for rest
    private Authentication authentication;


    @PostConstruct
    protected void init(){
        context = FacesContext.getCurrentInstance();
        userRequestData = userSessionData.getUser();
    }

    @Override
    public UserWithAuthentication getUserRequestData() {
        return userRequestData;
    }

    @Override
    public User getUserPersistedData() {
        if(userRequestData != null){
            return userService.getUserData(userRequestData);
        } else {
            return null;
        }
    }

    @Override
    public void applyAuthentication(final Authentication authentication) {
        this.authentication = authentication;
        this.userRequestData = authentication.getUser();
    }

    @Override
    public void onLoginRequired() {
        if(context != null){
            userSessionData.setRequestedView(context.getViewRoot().getViewId());
            final ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context
                    .getApplication().getNavigationHandler();
            navigationHandler.performNavigation(Navigation.DEFAULT_LOGIN_PAGE.getView());
        } else {
            throw new AuthenticationException(authentication);
        }
    }

    @Override
    public void onLoginFailed() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onPermissionMissed() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends RequestHelper> getType() {
        return AuthenticationRequestHelper.class;
    }
}
