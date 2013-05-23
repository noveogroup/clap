package com.noveogroup.clap.integration.auth;


/**
 * @author Andrey Sokolov
 */
public interface AuthenticationSystem {
    void authentifyUser(AuthenticationRequestHelper helper);
    String getSystemId();
}
