package com.noveogroup.clap.model.user;

import org.apache.shiro.authz.Permission;

/**
 * @author Andrey Sokolov
 */
public enum ClapPermission implements Permission {
    EDIT_REVISIONS,
    EDIT_PROJECTS,
    EDIT_USERS,;

    @Override
    public boolean implies(final Permission p) {
        return false;
    }
}
