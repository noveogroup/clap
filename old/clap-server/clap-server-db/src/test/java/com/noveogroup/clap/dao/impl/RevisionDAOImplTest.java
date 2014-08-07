package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.entity.revision.RevisionEntity;
import org.mockito.InjectMocks;

/**
 * @author Andrey Sokolov
 */
public class RevisionDAOImplTest extends AbstractDAOImplTest<RevisionDAOImpl> {

    @InjectMocks
    private RevisionDAOImpl revisionDAO;

    @Override
    protected RevisionDAOImpl getDAOImpl() {
        return revisionDAO;
    }

    @Override
    protected Class getEntityClass() {
        return RevisionEntity.class;
    }
}
