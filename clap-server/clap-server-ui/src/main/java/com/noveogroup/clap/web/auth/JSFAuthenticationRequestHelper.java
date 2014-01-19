package com.noveogroup.clap.web.auth;

import com.noveogroup.clap.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.rest.exception.AuthConstraintViolationException;
import com.noveogroup.clap.rest.exception.AuthenticationException;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.UserSessionData;
import com.noveogroup.clap.web.util.message.MessageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class JSFAuthenticationRequestHelper implements AuthenticationRequestHelper {

    @Inject
    private UserService userService;

    @Inject
    private UserSessionData userSessionData;

    private Authentication authentication;


    @PostConstruct
    protected void init() {
        authentication = userSessionData.getAuthentication();
    }

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
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            if (!userSessionData.isAuthenticated()) {
                userSessionData.setAuthenticated(true);
                userSessionData.setUser(user);
                if (StringUtils.isNotEmpty(userSessionData.getRequestedView())) {
                    final ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context
                            .getApplication().getNavigationHandler();
                    navigationHandler.performNavigation(userSessionData.getRequestedView());
                }
            }
        }
    }

    @Override
    public void onLoginRequired() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            userSessionData.setRequestedView(context.getViewRoot().getViewId());
            final ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context
                    .getApplication().getNavigationHandler();
            navigationHandler.performNavigation(Navigation.AUTH.getView());
        } else {
            throw new AuthenticationException(authentication);
        }
    }

    @Override
    public void onLoginFailed() {
        userSessionData.reset();
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    MessageUtils.getMessage(context, "error.authentication.failed"), null));
        } else {
            throw new AuthenticationException(authentication);
        }
    }

    @Override
    public void onConstraintViolate(List<ConstraintViolation> constraintViolationList) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            if (CollectionUtils.isNotEmpty(constraintViolationList)) {
                for (ConstraintViolation constraintViolation : constraintViolationList) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            MessageUtils.getMessage(context, "error.dynamic",
                                    new String[]{constraintViolation.getDescription()}),
                            null));
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        MessageUtils.getMessage(context, "error.permission.missed"), null));
            }
        } else {
            throw new AuthConstraintViolationException(constraintViolationList);
        }
    }

    @Override
    public Class<? extends RequestHelper> getType() {
        return AuthenticationRequestHelper.class;
    }
}
