package com.noveogroup.clap.service.user;

import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

/**
 * @author Andrey Sokolov
 */
@Stateless
@Interceptors(ClapMainInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class UserServiceImpl implements UserService {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private UserDAO userDAO;

    @Override
    public User updateUserData(final User user) {
        if(user == null){
            throw new IllegalArgumentException("user == null");
        }
        final String authenticationKey = user.getAuthenticationKey();
        if(StringUtils.isNotBlank(authenticationKey)){
            final UserEntity userEntity = userDAO.getUserByAuthenticationKey(authenticationKey);
            final User updatedModel = MAPPER.map(userEntity,User.class);
            return updatedModel;
        }
        final String login = user.getLogin();
        if(StringUtils.isNotBlank(login)){
            final UserEntity userEntity = userDAO.getUserByLogin(login);
            final User updatedModel = MAPPER.map(userEntity,User.class);
            return updatedModel;
        }
        throw new IllegalArgumentException("neither login nor authenticationKey provided");
    }


    @Transactional
    @Override
    public User createUser(final User user){
        UserEntity userEntity = MAPPER.map(user,UserEntity.class);
        userEntity = userDAO.persist(userEntity);
        return MAPPER.map(userEntity,User.class);
    }
}
