package com.noveogroup.clap.facade;

import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.transaction.Transactional;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({ClapMainInterceptor.class})
public class UsersFacade {

    @Inject
    private UserService userService;

    @Transactional
    public User createUser(final User user){
        return userService.createUser(user);
    }
}
