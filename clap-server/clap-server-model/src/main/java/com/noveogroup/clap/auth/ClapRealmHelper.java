package com.noveogroup.clap.auth;

import com.noveogroup.clap.service.user.UserService;

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
            Bean<UserService> bean = (Bean<UserService>) beanManager.resolve(beanManager.getBeans(UserService.class));
            UserService userService = (UserService) beanManager.getReference(bean, bean.getBeanClass(),
                    beanManager.createCreationalContext(bean));
            return userService;
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }
}
