package com.noveogroup.clap.converter;

import com.google.gson.Gson;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class RevisionConverter {

    private static final Mapper MAPPER = new DozerBeanMapper();

    private MessagesConverter messagesConverter = new MessagesConverter();

    public void setMessagesConverter(final MessagesConverter messagesConverter) {
        this.messagesConverter = messagesConverter;
    }

    public RevisionEntity map(final Revision revision) {
        return MAPPER.map(revision, RevisionEntity.class);
    }

    public Revision map(final RevisionEntity revision, final boolean mapMessages, final ConfigBean configBean) {
        final Revision ret = new Revision();
        map(ret, revision, mapMessages, configBean);
        return ret;
    }

    public RevisionWithApkStructure mapWithApkStructure(final RevisionEntity revision,
                                                        final boolean mapMessages, final ConfigBean configBean) {
        final RevisionWithApkStructure ret = new RevisionWithApkStructure();
        map(ret, revision, mapMessages, configBean);
        ret.setApkStructure(new Gson().fromJson(revision.getApkStructureJSON(), ApkStructure.class));
        return ret;
    }

    public void updateRevisionData(final RevisionEntity entityToUpdate, final Revision newData) {
        entityToUpdate.setRevisionType(newData.getRevisionType());
    }


    private void map(final Revision toMap, final RevisionEntity revision,
                     final boolean mapMessages,  final ConfigBean configBean) {
        toMap.setId(revision.getId());
        toMap.setHash(revision.getHash());
        toMap.setMessages(new ArrayList<BaseMessage>());

        final List<BaseMessageEntity> revisionMessages = revision.getMessages();
        if (mapMessages && CollectionUtils.isNotEmpty(revisionMessages)) {
            for (final BaseMessageEntity message : revisionMessages) {
                BaseMessage map = messagesConverter.map(message, configBean);
                map.setUploadedBy(message.getUploadedBy().getLogin());
                toMap.getMessages().add(map);
            }
        }
        final ProjectEntity project = revision.getProject();
        if (project != null) {
            toMap.setProjectId(project.getId());
        }
        toMap.setRevisionType(revision.getRevisionType());
        toMap.setTimestamp(revision.getTimestamp());
        final UserEntity mainPackageUploadedBy = revision.getMainPackageUploadedBy();
        if (mainPackageUploadedBy != null) {
            toMap.setMainPackageUploadedBy(mainPackageUploadedBy.getLogin());
        }
        final UserEntity specialPackageUploadedBy = revision.getSpecialPackageUploadedBy();
        if (specialPackageUploadedBy != null) {
            toMap.setSpecialPackageUploadedBy(specialPackageUploadedBy.getLogin());
        }
    }
}
