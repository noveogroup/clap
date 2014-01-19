package com.noveogroup.clap.dao;

import com.noveogroup.clap.entity.ProjectEntity;

/**
 * @author
 */
public interface ProjectDAO extends GenericDAO<ProjectEntity, Long> {

    ProjectEntity findProjectByExternalIdOrReturnNull(String externalId);

}
