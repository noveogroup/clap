package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.integration.DAOIntegration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.when;

/**
 * @author Andrey Sokolov
 */
public class UserDAOImplTest extends AbstractDAOImplTest<UserDAOImpl>{

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
