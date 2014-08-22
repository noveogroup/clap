package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import com.noveogroup.clap.service.revision.RevisionService;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class RevisionConverter extends BaseModelConverter implements Converter {

    @Inject
    private RevisionService revisionService;

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        if (s == null || !s.matches("\\d+")) {
            return null;
        }
        RevisionWithApkStructure revision = revisionService.getRevisionWithApkStructure(Long.valueOf(s));
        if (revision == null) {
            throw new ConverterException(new FacesMessage("Unknown ID: " + s));
        }
        return revision;
    }
}
