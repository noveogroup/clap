package com.noveogroup.clap.service.project;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.auth.Authentication;

import java.util.List;

/**
 * @author
 */
public interface ProjectService {


    Project createProject(Authentication authentication,Project project);

    Project save(Authentication authentication,Project project);

    Project findById(Authentication authentication,Long id);

    List<Project> findAllProjects(Authentication authentication);




}
