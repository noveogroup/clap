package com.noveogroup.clap.converter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.project.ImagedProject;
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

    private ProjectConverter projectConverter = new ProjectConverter();
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

    public void setProjectConverter(final ProjectConverter projectConverter) {
        this.projectConverter = projectConverter;
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
        List<ImagedProject> watchedProjects = Lists.newArrayList();
        toMap.setWatchedProjects(watchedProjects);
        final List<ProjectEntity> watchedProjectsEntities = mapWith.getWatchedProjects();
        if(watchedProjectsEntities != null){
            for(ProjectEntity projectEntity : watchedProjectsEntities){
                toMap.getWatchedProjects().add(projectConverter.mapToImagedProject(projectEntity, configBean, false));
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
