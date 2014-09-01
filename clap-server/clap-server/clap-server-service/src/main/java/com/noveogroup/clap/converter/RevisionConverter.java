package com.noveogroup.clap.converter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionVariant;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RevisionConverter {

    private static final Mapper MAPPER = new DozerBeanMapper();


    private RevisionVariantConverter variantConverter = new RevisionVariantConverter();


    public void setVariantConverter(final RevisionVariantConverter variantConverter) {
        this.variantConverter = variantConverter;
    }

    public RevisionEntity map(final Revision revision) {
        return MAPPER.map(revision, RevisionEntity.class);
    }

    public Revision map(final RevisionEntity revision) {
        final Revision ret = new Revision();
        map(ret, revision);
        return ret;
    }

    public void updateRevisionData(final RevisionEntity entityToUpdate, final Revision newData) {
        entityToUpdate.setRevisionType(newData.getRevisionType());
    }


    private void map(final Revision toMap, final RevisionEntity revision) {
        toMap.setId(revision.getId());
        toMap.setHash(revision.getHash());
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
            for (RevisionVariantEntity variantEntity : variantEntities) {
                mapVariant(toMap, variantEntity);
            }
        }
    }

    private void mapVariant(final Revision toMap,
                            final RevisionVariantEntity variantEntity) {
        RevisionVariant variant = variantConverter.map(variantEntity);
        if (variant != null) {
            toMap.getVariants().add(variant);
        }

    }
}
