package com.noveogroup.clap.model.user;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class UserWithPersistedAuth extends User {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .toString();
    }
}
