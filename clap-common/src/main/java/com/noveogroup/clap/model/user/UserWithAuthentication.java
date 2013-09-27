package com.noveogroup.clap.model.user;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrey Sokolov
 */
@XmlRootElement
public class UserWithAuthentication extends BaseUser {

    private String password;
    private String authenticationKey;

    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @XmlElement
    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(final String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("password", password)
                .append("authenticationKey", authenticationKey)
                .toString();
    }
}
