package com.noveogroup.clap.auth;

import com.noveogroup.clap.service.user.UserService;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Andrey Sokolov
 */
public class ClapRealmHelper {

    /**
     * I hate that fucking old school frameworks!!!!!!
     *
     * @return user service
     */
    public UserService getUserService() {
        try {
            BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
            Bean<UserService> userDAObean = (Bean<UserService>) beanManager.resolve(
                    beanManager.getBeans(UserService.class));
            CreationalContext<UserService> creationalContext = beanManager.createCreationalContext(null);
            UserService userService = userDAObean.create(creationalContext);
            return userService;
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }
}
