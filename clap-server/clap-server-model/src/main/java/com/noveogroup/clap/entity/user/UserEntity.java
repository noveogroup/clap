package com.noveogroup.clap.entity.user;

import com.noveogroup.clap.entity.BaseEntity;

import javax.persistence.*;

/**
 * @author Andrey Sokolov
 */
@Entity
@Table(name = "clapUsers")
@NamedQueries({
        @NamedQuery(name = "getUserByAuthenticationKey",
                query = "SELECT u FROM UserEntity u WHERE u.authenticationKey = :authenticationKey"),
        @NamedQuery(name = "getUserByLogin",
                query = "SELECT u FROM UserEntity u WHERE u.login = :login")
})
public class UserEntity extends BaseEntity {

    private String fullName;

    @Column(unique = true, nullable = false)
    private String login;

    private String password;

    @Column(unique = true, nullable = true)
    private String authenticationKey;


    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
