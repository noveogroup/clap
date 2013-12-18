package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.user.RequestUserModel;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
@SessionScoped
public class UserSessionData implements Serializable{

    private RequestUserModel user;

    private String requestedView;

    public String getRequestedView() {
        return requestedView;
    }

    public void setRequestedView(final String requestedView) {
        this.requestedView = requestedView;
    }

    public RequestUserModel getUser() {
        return user;
    }

    public void setUser(final RequestUserModel user) {
        this.user = user;
    }
}
