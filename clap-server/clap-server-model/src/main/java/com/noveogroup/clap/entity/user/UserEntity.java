package com.noveogroup.clap.entity.user;

import com.noveogroup.clap.entity.BaseEntity;

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
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("fullName='").append(fullName).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", authenticationKey='").append(authenticationKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
