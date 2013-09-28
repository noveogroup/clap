package com.noveogroup.clap.model.user;


/**
 * @author Andrey Sokolov
 */
public class User extends BaseUser {
    private String fullName;


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
        sb.append('}');
        return sb.toString();
    }
}
