package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.model.projects.StreamedImagedProject;

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
public class ProjectConverter extends BaseModelConverter implements Converter{

    @Inject
    private ProjectService projectService;

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        if (s == null || !s.matches("\\d+")) {
            return null;
        }
        ImagedProject project = projectService.findByIdWithImage(Long.valueOf(s));
        if (project == null) {
            throw new ConverterException(new FacesMessage("Unknown ID: " + s));
        }
        return new StreamedImagedProject(project);
    }
}
