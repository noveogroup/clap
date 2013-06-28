package com.noveogroup.clap.web.util.message;

import javax.faces.context.FacesContext;

/**
 * @author Andrey Sokolov
 */
public class MessageSupport {

    public String getMessage(final String messageName) {
        return MessageUtils.getMessage(FacesContext.getCurrentInstance(), messageName);
    }

    public String getMessage(final String messageName, final Object[] args) {
        return MessageUtils.getMessage(FacesContext.getCurrentInstance(), messageName, args);
    }

    public String getMessage(final String bundleVar, final String messageName) {
        return MessageUtils.getMessage(FacesContext.getCurrentInstance(), bundleVar, messageName);
    }

    public String getMessage(final String bundleVar, final String messageName, final Object[] args) {
        return MessageUtils.getMessage(FacesContext.getCurrentInstance(), bundleVar, messageName, args);
    }
}
