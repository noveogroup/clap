package com.noveogroup.clap.integration.db;

import com.noveogroup.clap.integration.DAOIntegration;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class DAOIntegrationImpl implements DAOIntegration{

    @PersistenceContext(unitName = "distribution_pu")
    protected EntityManager entityManager;

    @Override
    public EntityManager getClapEntityManager() {
        return entityManager;
    }
}
