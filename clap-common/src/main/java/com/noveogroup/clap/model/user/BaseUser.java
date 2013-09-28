package com.noveogroup.clap.model.user;


/**
 * @author Mikhail Demidov
 */
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
