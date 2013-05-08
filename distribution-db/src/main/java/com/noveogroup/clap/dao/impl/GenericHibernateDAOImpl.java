package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.GenericDAO;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public abstract class GenericHibernateDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {


    private Class<T> persistentClass;

    @PersistenceContext(unitName = "distribution_pu")
    protected EntityManager entityManager;


    public GenericHibernateDAOImpl() {

    }

    @PostConstruct
    public void setup() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public void removeById(ID id) {
        T obj = entityManager.find(persistentClass, id);
        entityManager.remove(obj);
    }


    @Override
    public T findById(ID id) {
        return entityManager.find(persistentClass, id);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public List<T> selectAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(persistentClass);
        Root<T> rc = cq.from(persistentClass);
        cq.select(rc);
        TypedQuery<T> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
