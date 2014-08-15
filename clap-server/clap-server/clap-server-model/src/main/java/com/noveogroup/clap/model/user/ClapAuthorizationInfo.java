package com.noveogroup.clap.model.user;

import com.google.common.base.Function;
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
    private List<ClapPermission> permissions = Lists.newArrayList();

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
        return Lists.transform(permissions,new Function<ClapPermission, String>() {
            @Override
            public String apply(final ClapPermission permission) {
                return permission.name();
            }
        });
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return Collections.emptyList();
    }
}
