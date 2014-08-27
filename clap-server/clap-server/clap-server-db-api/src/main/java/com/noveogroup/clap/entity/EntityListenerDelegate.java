package com.noveogroup.clap.entity;


/**
 * @author Andrey Sokolov
 */
public interface EntityListenerDelegate<T extends BaseEntity> {
    void postRemove(T entity);

}
