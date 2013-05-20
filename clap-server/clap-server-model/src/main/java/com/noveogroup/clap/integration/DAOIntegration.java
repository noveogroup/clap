package com.noveogroup.clap.integration;


import javax.persistence.EntityManager;

/**
 * @author Andrey Sokolov
 */
public interface DAOIntegration {
    EntityManager getClapEntityManager();
}
