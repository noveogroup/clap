package com.noveogroup.clap.service;

import com.noveogroup.clap.entity.Project;

/**
 * @author
 */
public interface ProjectService {

    String getName();

    Project save(Project project);

}
