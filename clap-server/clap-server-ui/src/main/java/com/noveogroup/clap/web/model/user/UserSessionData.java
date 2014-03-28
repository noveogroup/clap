package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.Role;
import com.noveogroup.clap.model.user.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

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

    public boolean hasPermission(final ClapPermission permission) {
        return hasPermission(permission.name());
    }

    public boolean hasPermission(final String string) {
        if (user != null) {
            final List<ClapPermission> clapPermissions = user.getClapPermissions();
            if (CollectionUtils.isNotEmpty(clapPermissions)) {
                final ClapPermission clapPermission = ClapPermission.valueOf(string);
                return clapPermissions.contains(clapPermission);
            }
        }
        return false;
    }

    public boolean isInRole(final String string) {
        if (user != null) {
            Role role = Role.valueOf(string);
            return user.getRole().equals(role);
        }
        return false;
    }
}
