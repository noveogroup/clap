package com.noveogroup.clap.dao.impl;

import com.google.common.collect.Lists;
import com.noveogroup.clap.dao.GenericDAO;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author mdemidov
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @PersistenceContext(unitName = "distribution_pu")
    protected EntityManager entityManager;

    private Class<T> persistentClass;

    public GenericDAOImpl() {

    }

    @PostConstruct
    public void setup() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T persist(final T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void remove(final T entity) {
        entityManager.remove(entity);
    }

    @Override
    public void removeById(final ID id) {
        final T obj = entityManager.find(persistentClass, id);
        entityManager.remove(obj);
    }


    @Override
    public T findById(final ID id) {
        return entityManager.find(persistentClass, id);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public List<T> selectAll() {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(persistentClass);
        final Root<T> rc = cq.from(persistentClass);
        cq.select(rc);
        final TypedQuery<T> query = entityManager.createQuery(cq);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return Lists.newArrayList();
        }

    }


    protected T getSingleResultOrNull(final Query query) {
        final List results = query.getResultList();
        if (results.size() == 0) {
            return null;
        } else if (results.size() == 1) {
            return (T) results.get(0);
        } else {
            throw new NonUniqueResultException();
        }
    }
}
