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

    public void setUser(User user) {
        this.user = user;
    }
}
