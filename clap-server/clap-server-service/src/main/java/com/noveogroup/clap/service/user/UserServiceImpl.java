package com.noveogroup.clap.service.user;

import com.noveogroup.clap.auth.AuthenticationRequired;
import com.noveogroup.clap.auth.PasswordsHashCalculator;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.exception.ClapUserNotFoundException;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserCreationModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors({ClapMainInterceptor.class})
public class UserServiceImpl implements UserService {

    private static final Mapper MAPPER = new DozerBeanMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @EJB
    private UserDAO userDAO;

    @WrapException
    @Override
    public UserWithPersistedAuth getUserWithPersistedAuth(final String login) {
        LOGGER.debug("user service impl call get user data");
        if (StringUtils.isBlank(login)) {
            throw new IllegalArgumentException("blank login");
        }
        final UserEntity userEntity = userDAO.getUserByLogin(login);
        if (userEntity != null) {
            return MAPPER.map(userEntity,UserWithPersistedAuth.class);
        } else {
            throw new ClapUserNotFoundException("requested login == " + login);
        }
    }

    @AuthenticationRequired
    @Override
    public User getUser(final String login) {
        final UserEntity userEntity = userDAO.getUserByLogin(login);
        final User user = MAPPER.map(userEntity,User.class);
        return user;
    }

    @AuthenticationRequired
    @WrapException
    @Override
    public User saveUser(final User user) {
        final UserEntity userEntity = userDAO.getUserByLogin(user.getLogin());
        userEntity.setLogin(user.getLogin());
        userEntity.setFullName(user.getFullName());
        //TODO when we will have more info
        final UserEntity updatedUserEnity = userDAO.persist(userEntity);
        final User updatedUser = MAPPER.map(updatedUserEnity,User.class);
        return updatedUser;
    }


    @WrapException
    @Override
    public void resetUserPassword(final RequestUserModel requestUserModel,final String newPassword) {
        final UserEntity userEntity = userDAO.getUserByLogin(requestUserModel.getLogin());
        final String oldPassHash = PasswordsHashCalculator.calculatePasswordHash(requestUserModel.getPassword());
        if(StringUtils.equals(oldPassHash,userEntity.getAuthenticationKey())){
            userEntity.setAuthenticationKey(PasswordsHashCalculator.calculatePasswordHash(newPassword));
            userDAO.persist(userEntity);
        } else {
            throw new ClapAuthenticationFailedException("old password incorrect");
        }
    }


    @AuthenticationRequired
    @WrapException
    @Override
    public User createUser(final UserCreationModel user) {
        UserEntity userEntity = MAPPER.map(user, UserEntity.class);
        userEntity.setAuthenticationKey(PasswordsHashCalculator.calculatePasswordHash(user.getPassword()));
        userEntity = userDAO.persist(userEntity);
        return MAPPER.map(userEntity, User.class);
    }
}
