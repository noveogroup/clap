package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.entity.user.UserEntity;
import org.mockito.InjectMocks;

/**
 * @author Andrey Sokolov
 */
public class UserDAOImplTest extends AbstractDAOImplTest<UserDAOImpl> {

    @InjectMocks
    private UserDAOImpl userDAO;

    @Override
    protected UserDAOImpl getDAOImpl() {
        return userDAO;
    }

    @Override
    protected Class getEntityClass() {
        return UserEntity.class;
    }
}
