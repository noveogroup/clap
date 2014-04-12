package com.noveogroup.clap.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Andrey Sokolov
 */
public abstract class AbstractDAOImplTest<T extends GenericDAOImpl> {

    @Mock
    protected EntityManager entityManager;

    protected abstract T getDAOImpl();

    protected abstract Class getEntityClass();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindById() throws Exception {
        final Object o = getEntityClass().newInstance();
        when(entityManager.find(getEntityClass(), null)).thenReturn(o);
        //assertEquals(o,getDAOImpl().findById(null));
    }
}
