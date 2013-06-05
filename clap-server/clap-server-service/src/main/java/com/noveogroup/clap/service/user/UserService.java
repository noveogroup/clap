package com.noveogroup.clap.service.user;

import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public interface UserService {
    /**
     * Update user model with data stored in DB
     * user is being identified by ids provided in non full model
     *
     * @param user non full model
     * @return filled model, not null
     */
    User updateUserData(User user);

    User createUser(User user);
}