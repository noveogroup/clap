package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.user.UserEntity;

/**
 * @author Andrey Sokolov
 */
public interface UserDAO extends GenericDAO<UserEntity, Long> {
    UserEntity getUserByToken(String authenticationKey);

    UserEntity getUserByLogin(String login);
}
