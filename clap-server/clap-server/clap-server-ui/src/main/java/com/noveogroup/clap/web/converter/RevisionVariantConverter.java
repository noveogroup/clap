package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.service.revision.RevisionService;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class RevisionVariantConverter extends BaseModelConverter implements Converter {

    @Inject
    private RevisionService revisionService;

    @Override
    protected Object getObject(final Long id) {
        return revisionService.getRevisionVariantWithApkStructure(id);
    }

    @Override
    protected String getErrorMessageId() {
        return "error.badRequest.variant";
    }
}
