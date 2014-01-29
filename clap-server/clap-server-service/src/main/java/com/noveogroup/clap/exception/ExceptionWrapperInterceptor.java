package com.noveogroup.clap.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @AroundInvoke
    public Object proceed(final InvocationContext context) throws Exception {
        final Method method = context.getMethod();
        //to exclude implicitly enabled EJBs
        if (method.isAnnotationPresent(WrapException.class)) {
            try {
                return context.proceed();
            } catch (PersistenceException e) {
                LOGGER.debug("exception wrapped : " + e);
                throw new ClapPersistenceException(e);
            }
        } else {
            return context.proceed();
        }
    }
}
