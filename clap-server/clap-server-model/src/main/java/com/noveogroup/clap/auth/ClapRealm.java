package com.noveogroup.clap.auth;

import com.noveogroup.clap.model.user.ClapAuthorizationInfo;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Clap inner Shiro realm, used to get users permissions and roles
 *
 * @author Andrey Sokolov
 */
public class ClapRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        final String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        final User user = getUserService().getUser(primaryPrincipal);
        return new ClapAuthorizationInfo(user);
    }

    /**
     * I hate that fucking old school frameworks!!!!!!
     *
     * @return user service
     */
    private UserService getUserService() {
        try {
            BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
            Bean<UserService> bean = (Bean<UserService>) beanManager.resolve(beanManager.getBeans(UserService.class));
            UserService userService = (UserService) beanManager.getReference(bean, bean.getBeanClass(),
                    beanManager.createCreationalContext(bean));
            return userService;
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
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

}
