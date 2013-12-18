package com.noveogroup.clap.service.user;

import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;

/**
 * @author Andrey Sokolov
 */
public interface UserService {

    User createUser(User user);

    /**
     * retrieve user with persisted authentication data
     *
     * @param login request user login
     * @return user model, not null
     */
    UserWithPersistedAuth getUserWithPersistedAuth(String login);
}
