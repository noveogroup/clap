package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.user.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class UsersModel implements Serializable {

    private User user;

    private UsersListDataModel usersListDataModel;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UsersListDataModel getUsersListDataModel() {
        return usersListDataModel;
    }

    public void setUsersListDataModel(final UsersListDataModel usersListDataModel) {
        this.usersListDataModel = usersListDataModel;
    }

}
