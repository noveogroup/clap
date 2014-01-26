package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.user.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class UserDAOImpl extends GenericHibernateDAOImpl<UserEntity, Long> implements UserDAO {

    private static final String GET_USER_BY_TOKEN = "getUserByToken";
    private static final String GET_USER_BY_TOKEN_PARAMETER = "token";

    private static final String GET_USER_BY_LOGIN = "getUserByLogin";
    private static final String GET_USER_BY_LOGIN_PARAMETER = "login";

    @Override
    public UserEntity getUserByToken(final String token) {
        final Query query = entityManager.createNamedQuery(GET_USER_BY_TOKEN);
        query.setParameter(GET_USER_BY_TOKEN_PARAMETER, token);
        try {
            final UserEntity userEntity = (UserEntity) query.getSingleResult();
            return userEntity;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserEntity getUserByLogin(final String login) {
        final Query query = entityManager.createNamedQuery(GET_USER_BY_LOGIN);
        query.setParameter(GET_USER_BY_LOGIN_PARAMETER, login);
        try {
            final UserEntity userEntity = (UserEntity) query.getSingleResult();
            return userEntity;
        } catch (NoResultException e) {
            return null;
        }

    }
}
