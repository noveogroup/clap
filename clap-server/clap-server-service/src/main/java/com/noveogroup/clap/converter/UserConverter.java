package com.noveogroup.clap.converter;

import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public class UserConverter {

    private RevisionConverter revisionConverter;

    private MessagesConverter messagesConverter;

    public User map(UserEntity userEntity){
        final User ret = new User();
        ret.setLogin(userEntity.getLogin());
        ret.setFullName(userEntity.getFullName());
        ret.setRole(userEntity.getRole());
        ret.setClapPermissions(userEntity.getClapPermissions());

        return null;
    }
}
