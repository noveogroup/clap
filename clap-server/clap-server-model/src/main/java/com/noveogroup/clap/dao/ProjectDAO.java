package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.project.ProjectEntity;

/**
 * @author
 */
public interface ProjectDAO extends GenericDAO<ProjectEntity, Long> {

    ProjectEntity findProjectByExternalIdOrReturnNull(String externalId);

}
