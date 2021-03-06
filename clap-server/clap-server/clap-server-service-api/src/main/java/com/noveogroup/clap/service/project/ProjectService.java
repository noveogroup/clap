package com.noveogroup.clap.service.project;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;

import java.io.File;
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

    Project save(Project project);

    Project findById(Long id);

    /**
     * @param id
     * @return the same entity as findById() but with iconFile mapped field
     */
    ImagedProject findByIdWithImage(Long id);

    List<Project> findAllProjects();

    /**
     * @return the same entity collection as findAllProjects() but with iconFile mapped fields
     */
    List<ImagedProject> findAllImagedProjects();

    void deleteProject(Project project);

    File getProjectIcon(long id);

    ImagedProject watchProject(long projectId);
}
