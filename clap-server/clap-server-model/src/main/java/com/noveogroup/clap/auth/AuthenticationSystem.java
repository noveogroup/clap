package com.noveogroup.clap.auth;


import com.noveogroup.clap.exception.ClapException;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationSystem {

    /**
     * @param helper
     * @param constraints authentication constraints list
     * @throws ClapException in case of auth not procceed
     */
    void authentifyUser(AuthenticationRequestHelper helper,
                        List<AuthenticationConstraint> constraints) throws ClapException;
    String getSystemId();
}
