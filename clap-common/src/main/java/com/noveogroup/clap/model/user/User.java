package com.noveogroup.clap.model.user;

/**
 * @author Andrey Sokolov
 */
public class User {
    private String fullName;
    private String login;
    private String password;
    private String authenticationKey;


    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(final String authenticationKey) {
        this.authenticationKey = authenticationKey;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("fullName='").append(fullName).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", authenticationKey='").append(authenticationKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
