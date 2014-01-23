package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.user.User;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class UserSessionData implements Serializable {

    private boolean authenticated = false;

    private User user;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getUserLink() {
        if (user != null) {
            final String fullName = user.getFullName();
            if (StringUtils.isNotEmpty(fullName)) {
                return fullName;
            } else {
                return user.getLogin();
            }
        } else {
            //shouldn't be there
            return "no user";
        }
    }

    public void reset() {
        authenticated = false;
        user = null;
    }
}