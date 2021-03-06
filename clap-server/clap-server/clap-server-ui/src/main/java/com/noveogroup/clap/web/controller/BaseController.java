package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.web.Navigation;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

public abstract class BaseController {

    protected void redirectTo(final Navigation navigation) {
        final ConfigurableNavigationHandler configurableNavigationHandler
                = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance()
                .getApplication().getNavigationHandler();
        configurableNavigationHandler.performNavigation(navigation.getView());
    }

    protected boolean isAjaxRequest() {
        PartialViewContext partialViewContext = FacesContext.getCurrentInstance().getPartialViewContext();
        return partialViewContext != null && partialViewContext.isAjaxRequest();
    }

    protected String getRequestParam(final String key) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            final ExternalContext externalContext = facesContext.getExternalContext();
            if (externalContext != null) {
                return externalContext.getRequestParameterMap().get(key);
            }
        }
        return null;
    }

}
