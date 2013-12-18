package com.noveogroup.clap.model.user;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class UserWithPersistedAuth extends BaseUser{
    private String authenticationKey;

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("authenticationKey", authenticationKey)
                .toString();
    }
}
