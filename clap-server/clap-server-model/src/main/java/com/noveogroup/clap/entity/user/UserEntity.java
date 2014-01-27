package com.noveogroup.clap.entity.user;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.Role;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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

    @ElementCollection(targetClass = ClapPermission.class)
    private List<ClapPermission> clapPermissions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mainPackageUploadedBy")
    private List<RevisionEntity> uploadedMainRevisions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "specialPackageUploadedBy")
    private List<RevisionEntity> uploadedSpecialRevisions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadedBy")
    private List<MessageEntity> uploadedMessages;

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

    public List<RevisionEntity> getUploadedMainRevisions() {
        return uploadedMainRevisions;
    }

    public void setUploadedMainRevisions(final List<RevisionEntity> uploadedMainRevisions) {
        this.uploadedMainRevisions = uploadedMainRevisions;
    }

    public List<RevisionEntity> getUploadedSpecialRevisions() {
        return uploadedSpecialRevisions;
    }

    public void setUploadedSpecialRevisions(final List<RevisionEntity> uploadedSpecialRevisions) {
        this.uploadedSpecialRevisions = uploadedSpecialRevisions;
    }

    public List<MessageEntity> getUploadedMessages() {
        return uploadedMessages;
    }

    public void setUploadedMessages(final List<MessageEntity> uploadedMessages) {
        this.uploadedMessages = uploadedMessages;
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
                .append("login", login)
                .append("authenticationKey", token)
                .append("role", role)
                .toString();
    }
}
