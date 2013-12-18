package com.noveogroup.clap.model.auth;

import com.noveogroup.clap.model.user.RequestUserModel;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class Authentication {

    private RequestUserModel user;


    public RequestUserModel getUser() {
        return user;
    }

    public void setUser(final RequestUserModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .toString();
    }
}
