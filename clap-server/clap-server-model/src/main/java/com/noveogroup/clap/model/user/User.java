package com.noveogroup.clap.model.user;


import com.google.common.collect.Lists;
import com.noveogroup.clap.model.message.BaseMessage;
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

    private List<BaseMessage> uploadedMessages = Lists.newArrayList();

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

    public List<BaseMessage> getUploadedMessages() {
        return uploadedMessages;
    }

    public void setUploadedMessages(final List<BaseMessage> uploadedMessages) {
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