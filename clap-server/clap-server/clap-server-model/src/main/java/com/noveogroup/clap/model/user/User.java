package com.noveogroup.clap.model.user;


import com.google.common.collect.Lists;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.revision.RevisionVariant;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class User extends BaseUser {
    private String fullName;

    private Role role;

    private List<ClapPermission> clapPermissions;

    private List<RevisionVariant> uploadedRevisionVariants = Lists.newArrayList();

    private List<Project> watchedProjects;

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

    public List<RevisionVariant> getUploadedRevisionVariants() {
        return uploadedRevisionVariants;
    }

    public void setUploadedRevisionVariants(final List<RevisionVariant> uploadedRevisionVariants) {
        this.uploadedRevisionVariants = uploadedRevisionVariants;
    }

    public List<Project> getWatchedProjects() {
        return watchedProjects;
    }

    public void setWatchedProjects(final List<Project> watchedProjects) {
        this.watchedProjects = watchedProjects;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName)
                .append("role", role)
                .toString();
    }
}
