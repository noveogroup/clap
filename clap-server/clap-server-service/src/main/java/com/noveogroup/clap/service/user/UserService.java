package com.noveogroup.clap.service.user;

import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserCreationModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;

/**
 * @author Andrey Sokolov
 */
public interface UserService {

    User createUser(UserCreationModel user);

    /**
     * retrieve user with persisted authentication data
     *
     * @param login request user login
     * @return user model, not null
     */
    UserWithPersistedAuth getUserWithPersistedAuth(String login);

    User getUser(String login);

    User saveUser(User user);

    void resetUserPassword(RequestUserModel requestUserModel,String newPassword);
}
