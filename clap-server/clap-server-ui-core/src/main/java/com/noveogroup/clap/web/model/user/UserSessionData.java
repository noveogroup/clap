package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.user.RequestUserModel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class UserSessionData implements Serializable{

    private boolean authenticated = false;

    private final RequestUserModel user = new RequestUserModel();

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

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void reset(){
        authenticated = false;
        user.setLogin(null);
        user.setPassword(null);
    }
}
