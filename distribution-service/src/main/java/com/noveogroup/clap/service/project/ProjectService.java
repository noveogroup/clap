package com.noveogroup.clap.service.project;

import com.noveogroup.clap.model.Project;

import java.util.List;

/**
 * @author
 */
public interface ProjectService {


    Project createProject(Project project);

    Project save(Project project);

    Project findById(Long id);

    List<Project> findAllProjects();




}
