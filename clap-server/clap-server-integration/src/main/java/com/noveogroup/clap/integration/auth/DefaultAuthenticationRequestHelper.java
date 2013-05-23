package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.exception.AuthenticationException;
import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.web.Navigation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class DefaultAuthenticationRequestHelper implements AuthenticationRequestHelper {

    //for JSF
    private FacesContext context;
    //for rest
    private Authentication authentication;


    @PostConstruct
    protected void init(){
        context = FacesContext.getCurrentInstance();
    }


    @Override
    public User getUserData() {
        //TODO
        return null;
    }

    @Override
    public void applyAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public void storeUserSessionData(final User user) {

    }

    @Override
    public void onLoginRequired() {
        if(context != null){
            final ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            configurableNavigationHandler.performNavigation(Navigation.DEFAULT_LOGIN_PAGE.getView());
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
