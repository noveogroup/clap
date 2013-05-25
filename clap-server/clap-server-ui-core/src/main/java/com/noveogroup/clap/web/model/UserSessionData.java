package com.noveogroup.clap.web.model;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
@SessionScoped
public class UserSessionData implements Serializable{

    private String requestedView;

    public String getRequestedView() {
        return requestedView;
    }

    public void setRequestedView(String requestedView) {
        this.requestedView = requestedView;
    }
}
