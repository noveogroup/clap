package com.noveogroup.clap.model.user;

import org.apache.shiro.authz.Permission;

/**
 * @author Andrey Sokolov
 */
public enum ClapPermission implements Permission {
    EDIT_REVISIONS,
    DELETE_REVISIONS,
    ADD_PROJECTS,
    EDIT_PROJECTS,
    DELETE_PROJECTS,
    EDIT_USERS,;

    @Override
    public boolean implies(final Permission p) {
        return false;
    }
}
