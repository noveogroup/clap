package com.noveogroup.clap.model.user;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Shiro AuthorizationInfo retrieved from Clap user model
 *
 * @author Andrey Sokolov
 */
public class ClapAuthorizationInfo implements AuthorizationInfo {

    private List<String> roles = Lists.newArrayList();
    private List<Permission> permissions = Lists.newArrayList();

    public ClapAuthorizationInfo(final User user) {
        if (user != null) {
            final Role role = user.getRole();
            if (role != null) {
                roles.add(role.name());
            }
            final List<ClapPermission> clapPermissions = user.getClapPermissions();
            if (CollectionUtils.isNotEmpty(clapPermissions)) {
                permissions.addAll(clapPermissions);
            }
        }
    }

    @Override
    public Collection<String> getRoles() {
        return roles;
    }

    @Override
    public Collection<String> getStringPermissions() {
        return Collections.emptyList();
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return permissions;
    }
}
