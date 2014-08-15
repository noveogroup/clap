package com.noveogroup.clap.web.model.user;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.User;
import org.primefaces.model.DualListModel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class UsersModel implements Serializable {

    private User user;

    private UsersListDataModel usersListDataModel;

    private DualListModel<ClapPermission> userPermissions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        setupUserPermissions(user);
    }

    public UsersListDataModel getUsersListDataModel() {
        return usersListDataModel;
    }

    public void setUsersListDataModel(final UsersListDataModel usersListDataModel) {
        this.usersListDataModel = usersListDataModel;
    }

    public DualListModel<ClapPermission> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(final DualListModel<ClapPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }

    private void setupUserPermissions(final User user){
        userPermissions = new DualListModel<ClapPermission>();
        List<ClapPermission> grantedPermissions = user.getClapPermissions();
        List<ClapPermission> availablePermissions = Lists.newArrayList(ClapPermission.values());
        for (ClapPermission clapPermission : grantedPermissions){
            availablePermissions.remove(clapPermission);
        }
        userPermissions.setSource(availablePermissions);
        userPermissions.setTarget(grantedPermissions);
    }
}
