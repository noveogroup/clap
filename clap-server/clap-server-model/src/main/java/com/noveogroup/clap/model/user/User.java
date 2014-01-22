package com.noveogroup.clap.model.user;


import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class User extends BaseUser {
    private String fullName;

    private Role role;

    private List<ClapPermission> clapPermissions;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public List<ClapPermission> getClapPermissions() {
        return clapPermissions;
    }

    public void setClapPermissions(final List<ClapPermission> clapPermissions) {
        this.clapPermissions = clapPermissions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName)
                .append("role", role)
                .toString();
    }
}
