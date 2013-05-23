package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.user.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class UserDAOImpl extends GenericHibernateDAOImpl<UserEntity, Long> implements UserDAO {

    private static final String GET_USER_BY_AUTHENTICATION_KEY = "getUserByAuthenticationKey";
    private static final String GET_USER_BY_AUTHENTICATION_KEY_PARAMETER = "authenticationKey";

    @Override
    public UserEntity getUserByAuthenticationKey(final String authenticationKey) {
        final Query query = entityManager.createNamedQuery(GET_USER_BY_AUTHENTICATION_KEY);
        query.setParameter(GET_USER_BY_AUTHENTICATION_KEY_PARAMETER, authenticationKey);
        final UserEntity userEntity = (UserEntity) query.getSingleResult();
        return userEntity;
    }
}
