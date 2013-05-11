package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.Project;

/**
 * @author
 */
public interface ProjectDAO extends GenericDAO<Project, Long> {

    Project findProjectByName(String name);
}
