package com.noveogroup.clap.model.user;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mikhail Demidov
 */
@XmlRootElement
public class BaseUser {

    private String login;


    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseUser{");
        sb.append("login='").append(login).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
