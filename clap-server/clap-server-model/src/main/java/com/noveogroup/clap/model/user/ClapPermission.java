package com.noveogroup.clap.model.user;

import org.apache.shiro.authz.Permission;

/**
 * @author Andrey Sokolov
 */
public enum ClapPermission implements Permission {
    EDIT_REVISIONS,
    DELETE_REVISIONS,
    SWITCH_REVISION_TO_DEVELOP,
    SWITCH_REVISION_TO_TEST,
    SWITCH_REVISION_TO_RELEASE,
    ADD_PROJECTS,
    EDIT_PROJECTS,
    DELETE_PROJECTS,
    EDIT_USERS,
    EDIT_PERMISSIONS,
    EDIT_ROLES;

    @Override
    public boolean implies(final Permission p) {
        return false;
    }
}
