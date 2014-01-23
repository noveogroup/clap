package com.noveogroup.clap.service.user;

import com.google.common.collect.Lists;
import com.noveogroup.clap.auth.PasswordsHashCalculator;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.exception.ClapUserNotFoundException;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.Role;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserCreationModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
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
            return MAPPER.map(userEntity, UserWithPersistedAuth.class);
        } else {
            throw new ClapUserNotFoundException("requested login == " + login);
        }
    }

    @RequiresAuthentication
    @Override
    public User getUser(final String login) {
        final UserEntity userEntity = userDAO.getUserByLogin(login);
        final User user = MAPPER.map(userEntity, User.class);
        return user;
    }

    @RequiresAuthentication
    @WrapException
    @Override
    public User saveUser(final User user) {
        final UserEntity userEntity = userDAO.getUserByLogin(user.getLogin());
        userEntity.setFullName(user.getFullName());
        userEntity.setRole(user.getRole());
        //TODO when we will have more info
        final UserEntity updatedUserEnity = userDAO.persist(userEntity);
        final User updatedUser = MAPPER.map(updatedUserEnity, User.class);
        return updatedUser;
    }


    @RequiresAuthentication
    @WrapException
    @Override
    public void resetUserPassword(final Authentication authentication, final String newPassword) {
        final UserEntity userEntity = userDAO.getUserByLogin(authentication.getLogin());
        final String oldPassHash = PasswordsHashCalculator.calculatePasswordHash(authentication.getPassword());
        if (StringUtils.equals(oldPassHash, userEntity.getAuthenticationKey())) {
            userEntity.setAuthenticationKey(PasswordsHashCalculator.calculatePasswordHash(newPassword));
            userDAO.persist(userEntity);
        } else {
            throw new ClapAuthenticationFailedException("old password incorrect");
        }
    }

    @RequiresAuthentication
    @RequiresRoles("ADMIN")
    @WrapException
    @Override
    public List<User> getUsers() {
        final List<UserEntity> userEntities = userDAO.selectAll();
        final List<User> users = Lists.newArrayList();
        for (UserEntity userEntity : userEntities) {
            users.add(MAPPER.map(userEntity, User.class));
        }
        return users;
    }


    @RequiresAuthentication
    @RequiresRoles("ADMIN")
    @WrapException
    @Override
    public User createUser(final UserCreationModel user) {
        user.setRole(Role.DEVELOPER);
        UserEntity userEntity = MAPPER.map(user, UserEntity.class);
        userEntity.setAuthenticationKey(PasswordsHashCalculator.calculatePasswordHash(user.getPassword()));
        userEntity = userDAO.persist(userEntity);
        return MAPPER.map(userEntity, User.class);
    }
}
