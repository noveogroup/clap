package com.noveogroup.clap.model.user;


import com.noveogroup.clap.model.message.Message;
import com.noveogroup.clap.model.revision.Revision;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class User extends BaseUser {
    private String fullName;

    private Role role;

    private List<ClapPermission> clapPermissions;

    private List<Revision> uploadedMainRevisions;

    private List<Revision> uploadedSpecialRevisions;

    private List<Message> uploadedMessages;

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

    public List<Revision> getUploadedMainRevisions() {
        return uploadedMainRevisions;
    }

    public void setUploadedMainRevisions(final List<Revision> uploadedMainRevisions) {
        this.uploadedMainRevisions = uploadedMainRevisions;
    }

    public List<Revision> getUploadedSpecialRevisions() {
        return uploadedSpecialRevisions;
    }

    public void setUploadedSpecialRevisions(final List<Revision> uploadedSpecialRevisions) {
        this.uploadedSpecialRevisions = uploadedSpecialRevisions;
    }

    public List<Message> getUploadedMessages() {
        return uploadedMessages;
    }

    public void setUploadedMessages(final List<Message> uploadedMessages) {
        this.uploadedMessages = uploadedMessages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fullName", fullName)
                .append("role", role)
                .toString();
    }
}
