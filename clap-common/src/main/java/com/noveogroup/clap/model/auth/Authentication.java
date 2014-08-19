package com.noveogroup.clap.model.auth;

import javax.validation.constraints.NotNull;

/**
 * Authentication data common model
 *
 * @author Andrey Sokolov
 */
public class Authentication {

    @NotNull
    private String login;

    @NotNull
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
