package com.noveogroup.clap.model.auth;

/**
 * Authentication data common model
 *
 * @author Andrey Sokolov
 */
public class Authentication {

    private String login;

    private String password;

    public Authentication() {
    }

    public Authentication(final String login, final String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
