package com.noveogroup.clap.model.auth;

import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public class Authentication {

    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Authentication{");
        sb.append("user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}
