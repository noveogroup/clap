package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.model.projects.StreamedImagedProject;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class ProjectConverter extends BaseModelConverter implements Converter {

    @Inject
    private ProjectService projectService;

    @Override
    protected Object getObject(final Long id) {
        return new StreamedImagedProject(projectService.findByIdWithImage(id));
    }
}
