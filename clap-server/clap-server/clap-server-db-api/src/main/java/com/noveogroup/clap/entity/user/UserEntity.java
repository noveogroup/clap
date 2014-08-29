package com.noveogroup.clap.entity.user;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
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
@Entity(name = "UserEntity")
@Table(name = "clapUsers")
@NamedQueries({
        @NamedQuery(name = "getUserByToken",
                query = "SELECT u FROM UserEntity u WHERE u.token = :token"),
        @NamedQuery(name = "getUserByLogin",
                query = "SELECT u FROM UserEntity u WHERE u.login = :login")
})
public class UserEntity extends BaseEntity {

    @Column(name = "full_name", nullable = true)
    private String fullName;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "token", unique = true, nullable = true)
    private String token;

    @Column(name = "pass_hash", nullable = true)
    private String hashedPassword;

    @Column(name = "role", nullable = false)
    private Role role;

    @ElementCollection(targetClass = ClapPermission.class)
    private List<ClapPermission> clapPermissions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "packageUploadedBy")
    private List<RevisionVariantEntity> uploadedRevisionVariants;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ProjectEntity> watchedProjects;

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


    public List<ClapPermission> getClapPermissions() {
        return clapPermissions;
    }

    public void setClapPermissions(final List<ClapPermission> clapPermissions) {
        this.clapPermissions = clapPermissions;
    }

    public List<RevisionVariantEntity> getUploadedRevisionVariants() {
        return uploadedRevisionVariants;
    }

    public void setUploadedRevisionVariants(final List<RevisionVariantEntity> uploadedRevisionVariants) {
        this.uploadedRevisionVariants = uploadedRevisionVariants;
    }

    public List<ProjectEntity> getWatchedProjects() {
        return watchedProjects;
    }

    public void setWatchedProjects(final List<ProjectEntity> watchedProjects) {
        this.watchedProjects = watchedProjects;
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
