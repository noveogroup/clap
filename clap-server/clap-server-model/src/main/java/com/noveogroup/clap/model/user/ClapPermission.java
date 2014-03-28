package com.noveogroup.clap.model.user;

import com.google.common.collect.Maps;
import org.apache.shiro.authz.Permission;

import java.util.Map;

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

    private static Map<String,ClapPermission> permissionMap = Maps.newHashMap();

    static {
        for(ClapPermission permission : values()){
            permissionMap.put(permission.name(),permission);
        }
    }


    @Override
    public boolean implies(final Permission p) {
        return false;
    }

    public static ClapPermission parseName(final String name){
        return permissionMap.get(name);
    }
}
