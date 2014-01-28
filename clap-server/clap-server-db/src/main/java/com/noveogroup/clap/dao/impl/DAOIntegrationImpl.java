package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.integration.DAOIntegration;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class DAOIntegrationImpl implements DAOIntegration {

    @PersistenceContext(unitName = "distribution_pu")
    protected EntityManager entityManager;

    @Override
    public EntityManager getClapEntityManager() {
        return entityManager;
    }
}
