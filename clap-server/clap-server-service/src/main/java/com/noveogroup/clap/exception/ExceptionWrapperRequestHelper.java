package com.noveogroup.clap.exception;

import com.noveogroup.clap.integration.DAOIntegration;
import com.noveogroup.clap.integration.RequestHelper;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * @author Andrey Sokolov
 */
public class ExceptionWrapperRequestHelper implements RequestHelper {

    @Inject
    private DAOIntegration daoIntegration;

    public EntityManager getEntityManager(){
        return daoIntegration.getClapEntityManager();
    }

    @Override
    public Class<? extends RequestHelper> getType() {
        return ExceptionWrapperRequestHelper.class;
    }
}
