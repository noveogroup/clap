package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.UserSessionData;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

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

    public String authentify() {
        //TODO
        final String requestedView = userSessionData.getRequestedView();
        if (StringUtils.isNotEmpty(requestedView)) {
            return requestedView;
        } else {
            return Navigation.HOME.getView();
        }
    }

    public String logout() {
        userSessionData.reset();
        SecurityUtils.getSubject().logout();
        return Navigation.HOME.getView();
    }
}
