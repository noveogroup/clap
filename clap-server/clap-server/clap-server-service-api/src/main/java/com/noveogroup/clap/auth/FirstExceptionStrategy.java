package com.noveogroup.clap.auth;

import com.noveogroup.clap.exception.ClapAuthenticationException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

/**
 * @author Andrey Sokolov
 */
public class FirstExceptionStrategy extends FirstSuccessfulStrategy {

    @Override
    public AuthenticationInfo afterAttempt(final Realm realm,
                                           final AuthenticationToken token,
                                           final AuthenticationInfo singleRealmInfo,
                                           final AuthenticationInfo aggregateInfo,
                                           final Throwable t) throws AuthenticationException {
        if (t != null) {
            if (t instanceof AuthenticationException) {
                throw (AuthenticationException) t;
            }
            if (t instanceof ClapAuthenticationException) {
                throw new AuthenticationException(t.getMessage(),t);
            }
        }
        return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
    }

}