package com.noveogroup.clap.exception;

import com.noveogroup.clap.integration.DAOIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;
import java.lang.reflect.Method;

/**
 * @author Andrey Sokolov
 */
@WrapException
@Interceptor
public class ExceptionWrapperInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionWrapperInterceptor.class);

    @Inject
    private DAOIntegration daoIntegration;

    @AroundInvoke
    public Object proceed(final InvocationContext context) throws Exception {
        final Method method = context.getMethod();
        //to exclude implicitly enabled EJBs
        if (method.isAnnotationPresent(WrapException.class)) {
            try {
                final Object ret = context.proceed();
                daoIntegration.getClapEntityManager().flush();

                return ret;
            } catch (PersistenceException e) {
                LOGGER.debug("exception wrapped : " + e);
                throw new ClapPersistenceException(e);
            }
        } else {
            return context.proceed();
        }

    }
}
