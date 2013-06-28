package com.noveogroup.clap.integration.auth;


import com.noveogroup.clap.exception.ClapException;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationSystem {

    /**
     * @param helper
     * @throws ClapException in case of auth not procceed
     */
    void authentifyUser(AuthenticationRequestHelper helper) throws ClapException;
    String getSystemId();
}
