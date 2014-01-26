package com.noveogroup.clap.auth;

import com.noveogroup.clap.model.user.ClapAuthorizationInfo;
import com.noveogroup.clap.model.user.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * Clap inner Shiro realm, used to get users permissions and roles
 *
 * @author Andrey Sokolov
 */
public class ClapRealm extends AuthorizingRealm {

    private ClapRealmHelper clapRealmHelper = new ClapRealmHelper();

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        final String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        final User user = clapRealmHelper.getUserService().getUser(primaryPrincipal);
        return new ClapAuthorizationInfo(user);
    }


    @Override
    public boolean supports(final AuthenticationToken token) {
        return false;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
            throws AuthenticationException {
        org.apache.shiro.mgt.SecurityManager securityManager = new DefaultSecurityManager();
        return null;
    }

    public void setClapRealmHelper(final ClapRealmHelper clapRealmHelper) {
        this.clapRealmHelper = clapRealmHelper;
    }
}
