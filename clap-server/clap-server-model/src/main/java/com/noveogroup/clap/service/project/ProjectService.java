package com.noveogroup.clap.service.project;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;

import java.util.List;

/**
 * @author
 */
public interface ProjectService {

    /**
     * creates project, throws exceptions if can't create
     *
     * @param project initial project data
     * @return persisted model
     */
    Project createProject(Project project);

    /**
     * find and returns project by provided model(uses externalId)
     * if project not exists - creates it
     * if project exists updates its data using provided model(null fields ignored)
     *
     * @param project criteria
     * @return persisted model
     */
    Project getCreateUpdateProject(Project project);

    //TODO check if needed
    Project save(Project project);

    Project findById(Long id);

    /**
     *
     * @param id
     * @return the same entity as findById() but with iconFile mapped field
     */
    ImagedProject findByIdWithImage(Long id);

    List<Project> findAllProjects();

    /**
     * @return the same entity collection as findAllProjects() but with iconFile mapped fields
     */
    List<ImagedProject> findAllImagedProjects();



}
