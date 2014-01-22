package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.integration.DAOIntegration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;

import static org.mockito.Mockito.when;

/**
 * @author Andrey Sokolov
 */
public abstract class AbstractDAOImplTest<T extends GenericHibernateDAOImpl> {

    @Mock
    protected DAOIntegration daoIntegration;

    @Mock
    protected EntityManager entityManager;

    protected abstract T getDAOImpl();

    protected Class getEntityClass() {
        return (Class<T>) ((ParameterizedType) getDAOImpl().getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(daoIntegration.getClapEntityManager()).thenReturn(entityManager);
    }


    @Test
    public void testFindById() throws Exception {
        final Object o = getEntityClass().newInstance();
        when(entityManager.find(getEntityClass(), null)).thenReturn(o);
        //assertEquals(o,getDAOImpl().findById(null));
    }
}
