package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.web.Navigation;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

public abstract class BaseController {

    protected void redirectTo(final Navigation navigation) {
        final ConfigurableNavigationHandler configurableNavigationHandler
                = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance()
                .getApplication().getNavigationHandler();
        configurableNavigationHandler.performNavigation(navigation.getView());
    }

}
