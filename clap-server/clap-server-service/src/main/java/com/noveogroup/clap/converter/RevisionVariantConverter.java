package com.noveogroup.clap.converter;

import com.google.gson.Gson;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.RevisionVariant;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;

/**
 * @author Andrey Sokolov
 */
public class RevisionVariantConverter extends BaseConverter<RevisionVariant, RevisionVariantEntity> {
    public RevisionVariant map(RevisionVariantEntity entity) {
        final RevisionVariant ret = new RevisionVariant();
        map(ret, entity);
        return ret;
    }

    public RevisionVariantWithApkStructure mapWithApkStructure(RevisionVariantEntity entity) {
        final RevisionVariantWithApkStructure revisionVariant = new RevisionVariantWithApkStructure();
        map(revisionVariant, entity);
        revisionVariant.setApkStructure(new Gson().fromJson(entity.getApkStructureJSON(), ApkStructure.class));
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
        model.setFullHash(entity.getFullHash());
        model.setPackageVariant(entity.getPackageVariant());
        model.setPackageUploadedBy(entity.getPackageUploadedBy().getLogin());
    }

    @Override
    public void map(final RevisionVariantEntity entity, final RevisionVariant model) {
        super.map(entity, model);
        entity.setFullHash(model.getFullHash());
        entity.setPackageVariant(model.getPackageVariant());
    }
}
