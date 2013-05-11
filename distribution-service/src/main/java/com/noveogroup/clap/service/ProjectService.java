package com.noveogroup.clap.service;

import com.noveogroup.clap.entity.Project;

/**
 * @author
 */
public interface ProjectService {


    Project createProject(Project project);

    String getName();

    Project save(Project project);


    Project findById(Long id);

}
