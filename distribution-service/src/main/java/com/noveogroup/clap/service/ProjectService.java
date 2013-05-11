package com.noveogroup.clap.service;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.model.ProjectDTO;

import java.util.List;

/**
 * @author
 */
public interface ProjectService {


    ProjectDTO createProject(ProjectDTO project);

    String getName();

    ProjectDTO save(ProjectDTO project);

    ProjectDTO findById(Long id);

    List<ProjectDTO> findAllProjects();

}
