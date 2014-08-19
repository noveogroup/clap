package com.noveogroup.clap.rest.auth;

import com.noveogroup.clap.auth.ClapRealmHelper;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;

import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
public class ClapRestRealm extends AuthenticatingRealm {

    private ClapRealmHelper clapRealmHelper = new ClapRealmHelper();

    @Inject
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
            throws AuthenticationException {
        ClapRestAuthenticationToken clapToken = (ClapRestAuthenticationToken) token;
        final User userByToken = clapRealmHelper.getUserService().getUserByToken((String) clapToken.getCredentials());
        final AuthenticationInfo ret = new SimpleAuthenticationInfo(userByToken.getLogin(),
                clapToken.getCredentials(), "ClapRestRealm");
        return ret;
    }

    @Override
    public boolean supports(final AuthenticationToken token) {
        return token instanceof ClapRestAuthenticationToken;
    }

    public void setClapRealmHelper(final ClapRealmHelper clapRealmHelper) {
        this.clapRealmHelper = clapRealmHelper;
    }
}
