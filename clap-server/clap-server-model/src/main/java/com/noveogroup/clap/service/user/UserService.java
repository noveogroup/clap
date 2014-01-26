package com.noveogroup.clap.service.user;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserCreationModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public interface UserService {

    /**
     * Create user by provided model
     * <p/>
     * Role is being reset to DEVELOPER
     *
     * @param user model
     * @return created user
     */
    User createUser(UserCreationModel user);

    /**
     * retrieve current user with token
     *
     * @return user model, not null
     */
    UserWithPersistedAuth getUserWithToken();


    /**
     * resolve login via security utils
     *
     * @return current user
     */
    User getUser();

    /**
     * resolve login via security utils
     *
     * @return current user
     */
    User getUser(boolean autocreate);

    User getUser(String login);

    User getUser(String login, boolean autocreate);

    User saveUser(User user);

    void resetUserPassword(String newPassword);

    void resetUserPassword(String login, String newPassword);

    List<User> getUsers();

    String getToken(Authentication authentication);

    User getUserByToken(String token);
}
