package com.noveogroup.clap.service.user;

import com.google.common.collect.Lists;
import com.noveogroup.clap.auth.PasswordsHashCalculator;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.UserConverter;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserConverter converter = new UserConverter();

    @EJB
    private UserDAO userDAO;

    @Inject
    private ConfigBean configBean;

    @Override
    public UserWithPersistedAuth getUserWithToken() {
        final String currentUserLogin = getCurrentUserLogin();
        final UserEntity userEntity = userDAO.getUserByLogin(currentUserLogin);
        if (userEntity != null) {
            return converter.mapWithPersistedAuth(userEntity,configBean);
        } else {
            throw new ClapUserNotFoundException("requested login == " + currentUserLogin);
        }
    }

    @RequiresAuthentication
    @Override
    public User getUser() {
        return getUser(false);
    }

    @RequiresAuthentication
    @Override
    public User getUser(boolean autocreate) {
        return getUser(getCurrentUserLogin(), autocreate);
    }

    @RequiresAuthentication
    @Override
    public User getUser(final String login) {
        return getUser(login, false);
    }

    @RequiresAuthentication
    @Override
    public User getUser(final String login, final boolean autocreate) {
        final UserEntity userEntity = userDAO.getUserByLogin(login);
        if (userEntity != null) {
            final User user = converter.map(userEntity,configBean);
            return user;
        } else if (autocreate) {
            final UserCreationModel userCreationModel = new UserCreationModel();
            userCreationModel.setLogin(login);
            return createUser(userCreationModel);
        } else {
            return null;
        }
    }

    @RequiresAuthentication
    @WrapException
    @Override
    public User saveUser(final User user) {
        final UserEntity userEntity = getUserByLogin(user);
        userEntity.setFullName(user.getFullName());
        //TODO when we will have more info
        return persistFlushAndReturnConverted(userEntity);
    }


    @RequiresAuthentication
    @WrapException
    @Override
    public void resetUserPassword(final String newPassword) {
        resetUserPassword(getCurrentUserLogin(), newPassword);
    }

    @RequiresAuthentication
    @RequiresRoles("ADMIN")
    @Override
    public void resetUserPassword(String login, String newPassword) {
        final UserEntity userEntity = userDAO.getUserByLogin(login);
        userEntity.setHashedPassword(PasswordsHashCalculator.calculatePasswordHash(newPassword));
        updateToken(userEntity);
        userDAO.persist(userEntity);
        userDAO.flush();
    }

    @RequiresAuthentication
    @Override
    public List<User> getUsers() {
        final List<UserEntity> userEntities = userDAO.selectAll();
        final List<User> users = Lists.newArrayList();
        for (UserEntity userEntity : userEntities) {
            users.add(converter.map(userEntity,configBean));
        }
        return users;
    }

    @Override
    public String getToken(final Authentication authentication) {
        final String login = authentication.getLogin();
        final UserEntity userByLogin = userDAO.getUserByLogin(login);
        if (userByLogin != null) {
            final String password = authentication.getPassword();
            if (StringUtils.equals(userByLogin.getHashedPassword(),
                    PasswordsHashCalculator.calculatePasswordHash(password))) {
                updateToken(userByLogin);
                userDAO.persist(userByLogin);
                return userByLogin.getToken();
            } else {
                throw new ClapAuthenticationFailedException("login or password incorrect");
            }
        } else {
            throw new ClapUserNotFoundException("user " + login + " not found");
        }
    }

    @Override
    public User getUserByToken(final String token) {
        final UserEntity userEntity = userDAO.getUserByToken(token);
        if (userEntity != null) {
            final User user = converter.map(userEntity,configBean);
            return user;
        }
        return null;
    }

    @RequiresAuthentication
    @RequiresRoles("ADMIN")
    @WrapException
    @Override
    public User createUser(final UserCreationModel user) {
        user.setRole(Role.DEVELOPER);
        UserEntity userEntity = converter.map(user);
        final String password = user.getPassword();
        if (password != null) {
            userEntity.setHashedPassword(PasswordsHashCalculator.calculatePasswordHash(password));
        }
        updateToken(userEntity);
        userEntity = userDAO.persist(userEntity);
        userDAO.flush();
        return converter.map(userEntity,configBean);
    }

    private void updateToken(final UserEntity userEntity) {
        LOGGER.debug("token for " + userEntity.getLogin() + " updated");
        final String token = UUID.randomUUID().toString();
        userEntity.setToken(token);
    }


    public String getCurrentUserLogin() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            final String login = (String) subject.getPrincipals().getPrimaryPrincipal();
            return login;
        } else {
            throw new IllegalStateException("security utils not prepared");
        }
    }

    @RequiresPermissions("EDIT_PERMISSIONS")
    @RequiresAuthentication
    @WrapException
    @Override
    public User editPermissions(final User user) {
        final UserEntity userEntity = getUserByLogin(user);
        userEntity.setClapPermissions(user.getClapPermissions());
        return persistFlushAndReturnConverted(userEntity);
    }

    @RequiresPermissions("EDIT_ROLES")
    @RequiresAuthentication
    @WrapException
    @Override
    public User editRole(final User user) {
        final UserEntity userEntity = getUserByLogin(user);
        userEntity.setRole(user.getRole());
        return persistFlushAndReturnConverted(userEntity);
    }

    private UserEntity getUserByLogin(final User user) {
        return userDAO.getUserByLogin(user.getLogin());
    }

    private User persistFlushAndReturnConverted(final UserEntity userEntity) {
        final UserEntity updatedUserEnity = userDAO.persist(userEntity);
        userDAO.flush();
        final User updatedUser = converter.map(updatedUserEnity,configBean);
        return updatedUser;
    }

    public void setConverter(final UserConverter converter) {
        this.converter = converter;
    }
}
