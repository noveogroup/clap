package com.noveogroup.clap.service.project;

import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.model.revision.RevisionDTO;

import java.util.List;

/**
 * @author
 */
public interface ProjectService {


    ProjectDTO createProject(ProjectDTO project);

    ProjectDTO save(ProjectDTO project);

    ProjectDTO findById(Long id);

    List<ProjectDTO> findAllProjects();




}
