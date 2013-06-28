package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.entity.ProjectEntity;
import org.mockito.InjectMocks;

/**
 * @author Andrey Sokolov
 */
public class ProjectDAOImplTest extends AbstractDAOImplTest<ProjectDAOImpl> {

    @InjectMocks
    private ProjectDAOImpl projectDAO;

    @Override
    protected ProjectDAOImpl getDAOImpl() {
        return projectDAO;
    }

}