package com.noveogroup.clap.converter;

import com.google.gson.Gson;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.RevisionVariant;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class RevisionVariantConverter extends BaseConverter<RevisionVariant, RevisionVariantEntity> {

    private MessagesConverter messagesConverter = new MessagesConverter();

    public void setMessagesConverter(final MessagesConverter messagesConverter) {
        this.messagesConverter = messagesConverter;
    }

    public RevisionVariant map(RevisionVariantEntity entity) {
        final RevisionVariant ret = new RevisionVariant();
        map(ret, entity);
        return ret;
    }

    public RevisionVariantWithApkStructure mapWithApkStructure(final RevisionVariantEntity entity,
                                                               final ConfigBean configBean) {
        final RevisionVariantWithApkStructure revisionVariant = new RevisionVariantWithApkStructure();
        map(revisionVariant, entity);
        revisionVariant.setApkStructure(new Gson().fromJson(entity.getApkStructureJSON(), ApkStructure.class));
        final List<BaseMessageEntity> revisionMessages = entity.getMessages();
        if (CollectionUtils.isNotEmpty(revisionMessages)) {
            for (final BaseMessageEntity message : revisionMessages) {
                BaseMessage map = messagesConverter.map(message, configBean);
                revisionVariant.getMessages().add(map);
            }
        }
        return revisionVariant;
    }


    public RevisionVariantEntity map(RevisionVariant revisionVariant) {
        final RevisionVariantEntity revisionVariantEntity = new RevisionVariantEntity();
        map(revisionVariantEntity, revisionVariant);
        return revisionVariantEntity;
    }


    @Override
    public void map(final RevisionVariant model, final RevisionVariantEntity entity) {
        super.map(model, entity);
        model.setRevisionId(entity.getRevision().getId());
        model.setProjectId(entity.getRevision().getProject().getId());
        model.setFullHash(entity.getFullHash());
        model.setPackageVariant(entity.getPackageVariant());
        if (entity.getPackageUploadedBy() != null) {
            model.setPackageUploadedBy(entity.getPackageUploadedBy().getLogin());
        }
        model.setMessages(new ArrayList<BaseMessage>());

    }

    @Override
    public void map(final RevisionVariantEntity entity, final RevisionVariant model) {
        super.map(entity, model);
        entity.setFullHash(model.getFullHash());
        entity.setPackageVariant(model.getPackageVariant());
    }
}
