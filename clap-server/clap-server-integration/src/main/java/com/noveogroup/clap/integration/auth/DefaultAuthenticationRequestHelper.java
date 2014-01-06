package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.rest.exception.AuthenticationException;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.integration.AuthenticationIntegrationView;
import com.noveogroup.clap.web.model.user.UserSessionData;
import com.noveogroup.clap.web.util.message.MessageUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class DefaultAuthenticationRequestHelper implements AuthenticationRequestHelper {

    @Inject
    private UserService userService;

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private AuthenticationIntegrationView authenticationIntegrationView;

    //for JSF
    private FacesContext context;
    //for rest
    private Authentication authentication;


    @PostConstruct
    protected void init() {
        context = FacesContext.getCurrentInstance();
    }

    @Override
    public RequestUserModel getUserRequestData() {
        if (authentication != null) {
            return authentication.getUser();
        } else {
            return userSessionData.getUser();
        }
    }

    @Override
    public UserWithPersistedAuth getUserPersistedData() {
        RequestUserModel userRequestData = getUserRequestData();
        if (userRequestData != null) {
            return userService.getUserWithPersistedAuth(userRequestData.getLogin());
        } else {
            throw new IllegalStateException("user request data can't be null");
        }
    }

    @Override
    public void applyAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public void onLoginSuccessfull() {
        if (!userSessionData.isAuthenticated()) {
            userSessionData.setAuthenticated(true);
            if (context != null) {
                final ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context
                        .getApplication().getNavigationHandler();
                navigationHandler.performNavigation(userSessionData.getRequestedView());
            }
        }
    }

    @Override
    public void onLoginRequired() {
        if (context != null) {
            userSessionData.setRequestedView(context.getViewRoot().getViewId());
            final ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context
                    .getApplication().getNavigationHandler();
            navigationHandler.performNavigation(authenticationIntegrationView.viewName());
        } else {
            throw new AuthenticationException(authentication);
        }
    }

    @Override
    public void onLoginFailed() {
        userSessionData.reset();
        if (context != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    MessageUtils.getMessage(context, "error.authentication.failed"), null));
        }
    }

    @Override
    public void onPermissionMissed() {
        if (context != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    MessageUtils.getMessage(context, "error.permission.missed"), null));
        }
    }

    @Override
    public Class<? extends RequestHelper> getType() {
        return AuthenticationRequestHelper.class;
    }
}
