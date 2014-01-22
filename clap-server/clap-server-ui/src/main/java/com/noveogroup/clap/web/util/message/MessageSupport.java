package com.noveogroup.clap.web.util.message;

import javax.faces.application.FacesMessage;
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

    public void addMessage(final String messageId) {
        addMessage(null, messageId);
    }

    public void addMessage(final String componentId, final String messageId) {
        addMessage(componentId, new FacesMessage(getMessage(messageId)));
    }

    public void addMessage(final String componentId, final FacesMessage facesMessage) {
        FacesContext.getCurrentInstance().addMessage(componentId, facesMessage);
    }
}
