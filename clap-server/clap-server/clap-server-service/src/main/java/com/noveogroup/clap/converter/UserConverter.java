package com.noveogroup.clap.converter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.revision.RevisionVariant;
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
    private RevisionVariantConverter variantConverter = new RevisionVariantConverter();

    private MessagesConverter messagesConverter = new MessagesConverter();

    public User map(final UserEntity userEntity, final ConfigBean configBean) {
        final User ret = new User();
        map(ret, userEntity, configBean);
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

    public UserWithPersistedAuth mapWithPersistedAuth(final UserEntity userEntity, final ConfigBean configBean) {
        final UserWithPersistedAuth ret = new UserWithPersistedAuth();
        map(ret, userEntity, configBean);
        ret.setToken(userEntity.getToken());
        return ret;
    }

    private void map(final User toMap, final UserEntity mapWith, final ConfigBean configBean) {
        toMap.setLogin(mapWith.getLogin());
        toMap.setFullName(mapWith.getFullName());
        toMap.setRole(mapWith.getRole());
        List<ClapPermission> permissions = Lists.newArrayList();
        if (mapWith.getClapPermissions() != null) {
            permissions.addAll(mapWith.getClapPermissions());
        }
        toMap.setClapPermissions(permissions);
        List<BaseMessage> messages = Lists.newArrayList();
        toMap.setUploadedMessages(messages);
        if (mapWith.getUploadedMessages() != null) {
            for (BaseMessageEntity messageEntity : mapWith.getUploadedMessages()) {
                final BaseMessage message = messagesConverter.map(messageEntity, configBean);
                if (message != null) {
                    messages.add(message);
                }
            }
        }
        List<RevisionVariant> uploadedVariants = Lists.newArrayList();
        toMap.setUploadedRevisionVariants(uploadedVariants);
        if (mapWith.getUploadedRevisionVariants() != null) {
            for (RevisionVariantEntity revisionEntity : mapWith.getUploadedRevisionVariants()) {
                final RevisionVariant revision = variantConverter.map(revisionEntity);
                if (revision != null) {
                    uploadedVariants.add(revision);
                }
            }
        }
    }
}
