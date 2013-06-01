package com.noveogroup.clap.integration.auth;


/**
 * @author Andrey Sokolov
 */
public interface AuthenticationSystem {
    /**
     * @param helper
     * @return true if user authentified and accepted
     */
    boolean authentifyUser(AuthenticationRequestHelper helper);
    String getSystemId();
}
