package com.noveogroup.clap.converter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionVariant;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
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

    private RevisionVariantConverter variantConverter = new RevisionVariantConverter();

    public void setMessagesConverter(final MessagesConverter messagesConverter) {
        this.messagesConverter = messagesConverter;
    }

    public void setVariantConverter(final RevisionVariantConverter variantConverter) {
        this.variantConverter = variantConverter;
    }

    public RevisionEntity map(final Revision revision) {
        return MAPPER.map(revision, RevisionEntity.class);
    }

    public Revision map(final RevisionEntity revision, final boolean mapMessages, final ConfigBean configBean) {
        final Revision ret = new Revision();
        map(ret, revision, mapMessages, configBean, false);
        return ret;
    }

    public RevisionWithApkStructure mapWithApkStructure(final RevisionEntity revision,
                                                        final boolean mapMessages, final ConfigBean configBean) {
        final RevisionWithApkStructure ret = new RevisionWithApkStructure();
        map(ret, revision, mapMessages, configBean, true);
        return ret;
    }

    public void updateRevisionData(final RevisionEntity entityToUpdate, final Revision newData) {
        entityToUpdate.setRevisionType(newData.getRevisionType());
    }


    private void map(final Revision toMap, final RevisionEntity revision,
                     final boolean mapMessages, final ConfigBean configBean, final boolean mapWithApkStructure) {
        toMap.setId(revision.getId());
        toMap.setHash(revision.getHash());
        toMap.setMessages(new ArrayList<BaseMessage>());

        final List<BaseMessageEntity> revisionMessages = revision.getMessages();
        if (mapMessages && CollectionUtils.isNotEmpty(revisionMessages)) {
            for (final BaseMessageEntity message : revisionMessages) {
                BaseMessage map = messagesConverter.map(message, configBean);
                toMap.getMessages().add(map);
            }
        }
        final ProjectEntity project = revision.getProject();
        if (project != null) {
            toMap.setProjectId(project.getId());
        }
        toMap.setRevisionType(revision.getRevisionType());
        toMap.setTimestamp(revision.getTimestamp());

        final List<RevisionVariantEntity> variantEntities = revision.getVariants();
        if (variantEntities != null) {
            List<RevisionVariant> variants = Lists.newArrayList();
            toMap.setVariants(variants);
            if (mapWithApkStructure) {
                List<RevisionVariantWithApkStructure> variantWithApkStructureList = Lists.newArrayList();
                ((RevisionWithApkStructure) toMap).setVariantWithApkStructureList(variantWithApkStructureList);
            }
            for (RevisionVariantEntity variantEntity : variantEntities) {
                mapVariant(toMap,variantEntity,mapWithApkStructure);
            }
        }
    }

    private void mapVariant(final Revision toMap,
                            final RevisionVariantEntity variantEntity, final boolean mapWithApkStructure) {
        RevisionVariant variant = null;
        if(mapWithApkStructure) {
            RevisionVariantWithApkStructure withApkStructure = variantConverter.mapWithApkStructure(variantEntity);
            ((RevisionWithApkStructure) toMap).getVariantWithApkStructureList().add(withApkStructure);
            variant = withApkStructure;
        }
        if (variant == null) {
            variant = variantConverter.map(variantEntity);
        }
        toMap.getVariants().add(variant);
    }
}
