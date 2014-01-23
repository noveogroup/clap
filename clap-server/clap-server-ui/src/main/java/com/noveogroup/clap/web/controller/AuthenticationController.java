package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.UserSessionData;
import org.apache.shiro.SecurityUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
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

    public String logout() {
        userSessionData.reset();
        SecurityUtils.getSubject().logout();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return Navigation.HOME.getView();
    }
}
