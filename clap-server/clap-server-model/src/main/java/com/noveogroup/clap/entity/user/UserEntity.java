package com.noveogroup.clap.entity.user;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.model.user.Role;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Andrey Sokolov
 */
@Entity
@Table(name = "clapUsers")
@NamedQueries({
        @NamedQuery(name = "getUserByToken",
                query = "SELECT u FROM UserEntity u WHERE u.token = :token"),
        @NamedQuery(name = "getUserByLogin",
                query = "SELECT u FROM UserEntity u WHERE u.login = :login")
})
public class UserEntity extends BaseEntity {

    private String fullName;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(unique = true, nullable = true)
    private String token;

    @Column(nullable = true)
    private String hashedPassword;

    @Column(nullable = false)
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String authenticationKey) {
        this.token = authenticationKey;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(final String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName)
                .append("login", login)
                .append("authenticationKey", token)
                .append("role", role)
                .toString();
    }
}
