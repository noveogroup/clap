package com.noveogroup.clap.entity;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PostRemove;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseEntityListener<T extends EntityListenerDelegate,E extends BaseEntity> {

    protected abstract Class<T> getDelegateClass();



    @PostRemove
    public void postRemove(E entity) {
        T delegate = getDelegate();
        if (delegate != null) {
            delegate.postRemove(entity);
        }
    }

    /**
     * unfortunately simple @Inject not works
     *
     * @return delegate
     */
    public T getDelegate() {
        try {
            BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
            Bean<T> userDAObean =
                    (Bean<T>) beanManager.resolve(
                            beanManager.getBeans(getDelegateClass()));
            CreationalContext<T> creationalContext =
                    beanManager.createCreationalContext(null);
            T listenerDelegate = userDAObean.create(creationalContext);
            return listenerDelegate;
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }
}
