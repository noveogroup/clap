package com.noveogroup.clap.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.lang.annotation.Annotation;

/**
 * @author Mikhail Demidov
 */
public class TransactionInterceptor {


    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionInterceptor.class);

    @Resource
    private UserTransaction userTransaction;


    @AroundInvoke
    public Object businessIntercept(InvocationContext ctx) throws SystemException {
        Object result = null;
        Annotation[] methodAnnotations = ctx.getMethod().getDeclaredAnnotations();
        if (methodAnnotations.length > 0) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof Transactional) {
                    try {
                        userTransaction.begin();
                        result = ctx.proceed();
                        userTransaction.commit();
                    } catch (IllegalStateException e) {
                        LOGGER.error("Transaction error " + e.getMessage(), e);
                        userTransaction.rollback();
                    } catch (Exception e) {
                        LOGGER.error("Transaction error " + e.getMessage(), e);
                        userTransaction.rollback();
                    }
                    return result;
                }
            }
        }
        try {
            result = ctx.proceed();
        } catch (Exception e) {
            LOGGER.error("error beyond transaction " + e.getMessage(), e);
        }
        return result;
    }

}
