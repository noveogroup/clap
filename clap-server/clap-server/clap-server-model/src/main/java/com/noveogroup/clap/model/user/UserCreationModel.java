package com.noveogroup.clap.model.user;

/**
 * @author Andrey Sokolov
 */
public class UserCreationModel extends User {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
