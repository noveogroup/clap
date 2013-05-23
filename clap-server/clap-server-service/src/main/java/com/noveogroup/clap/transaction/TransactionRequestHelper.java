package com.noveogroup.clap.transaction;

import com.noveogroup.clap.integration.RequestHelper;

import javax.annotation.Resource;
import javax.transaction.UserTransaction;

/**
 * @author Andrey Sokolov
 */
public class TransactionRequestHelper implements RequestHelper {

    @Resource
    private UserTransaction userTransaction;

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    @Override
    public Class<? extends RequestHelper> getType() {
        return TransactionRequestHelper.class;
    }
}
