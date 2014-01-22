package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.Role;
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

    private final Authentication authentication = new Authentication();

    private User user;

    private String requestedView;

    public String getRequestedView() {
        return requestedView;
    }

    public void setRequestedView(final String requestedView) {
        this.requestedView = requestedView;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

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
            return authentication.getLogin();
        }
    }

    public boolean isAdmin() {
        if (user != null) {
            return Role.ADMIN.equals(user.getRole());
        } else {
            return false;
        }
    }

    public void reset() {
        authenticated = false;
        authentication.setLogin(null);
        authentication.setPassword(null);
    }
}
