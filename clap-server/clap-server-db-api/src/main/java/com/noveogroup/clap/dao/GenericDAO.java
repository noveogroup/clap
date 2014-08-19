package com.noveogroup.clap.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>  entity type
 * @param <ID> primary key
 * @author mdemidov
 */
public interface GenericDAO<T, ID extends Serializable> {

    public T persist(T entity);

    public void remove(T entity);

    public void removeById(ID id);

    public T findById(ID id);

    public List<T> selectAll();

    void flush();

}
