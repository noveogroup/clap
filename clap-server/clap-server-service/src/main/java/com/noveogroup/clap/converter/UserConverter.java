package com.noveogroup.clap.converter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserCreationModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class UserConverter {

    private RevisionConverter revisionConverter = new RevisionConverter();

    private MessagesConverter messagesConverter = new MessagesConverter();

    public User map(UserEntity userEntity) {
        final User ret = new User();
        map(ret, userEntity);
        return ret;
    }

    public UserEntity map(final UserCreationModel user) {
        final UserEntity ret = new UserEntity();
        ret.setLogin(user.getLogin());
        ret.setFullName(user.getFullName());
        ret.setHashedPassword(user.getPassword());
        ret.setClapPermissions(user.getClapPermissions());
        ret.setRole(user.getRole());
        return ret;
    }

    public void setRevisionConverter(final RevisionConverter revisionConverter) {
        this.revisionConverter = revisionConverter;
    }

    public void setMessagesConverter(final MessagesConverter messagesConverter) {
        this.messagesConverter = messagesConverter;
    }

    public UserWithPersistedAuth mapWithPersistedAuth(final UserEntity userEntity) {
        final UserWithPersistedAuth ret = new UserWithPersistedAuth();
        map(ret, userEntity);
        ret.setToken(userEntity.getToken());
        return ret;
    }

    private void map(final User toMap, final UserEntity mapWith) {
        toMap.setLogin(mapWith.getLogin());
        toMap.setFullName(mapWith.getFullName());
        toMap.setRole(mapWith.getRole());
        List<ClapPermission> permissions = Lists.newArrayList();
        permissions.addAll(mapWith.getClapPermissions());
        toMap.setClapPermissions(permissions);
        List<BaseMessage> messages = Lists.newArrayList();
        toMap.setUploadedMessages(messages);
        for (BaseMessageEntity messageEntity : mapWith.getUploadedMessages()) {
            messages.add(messagesConverter.map(messageEntity));
        }
        List<Revision> mainRevisions = Lists.newArrayList();
        toMap.setUploadedMainRevisions(mainRevisions);
        for (RevisionEntity revisionEntity : mapWith.getUploadedMainRevisions()) {
            mainRevisions.add(revisionConverter.map(revisionEntity));
        }
        List<Revision> specialRevisions = Lists.newArrayList();
        toMap.setUploadedSpecialRevisions(specialRevisions);
        for (RevisionEntity revisionEntity : mapWith.getUploadedSpecialRevisions()) {
            specialRevisions.add(revisionConverter.map(revisionEntity));
        }
    }
}
