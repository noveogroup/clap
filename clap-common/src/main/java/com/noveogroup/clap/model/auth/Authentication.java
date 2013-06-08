package com.noveogroup.clap.model.auth;

import com.noveogroup.clap.model.user.UserWithAuthentication;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class Authentication {

    private UserWithAuthentication user;


    public UserWithAuthentication getUser() {
        return user;
    }

    public void setUser(final UserWithAuthentication user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .toString();
    }
}
