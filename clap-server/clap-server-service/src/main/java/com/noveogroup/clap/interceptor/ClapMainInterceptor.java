package com.noveogroup.clap.interceptor;

import com.noveogroup.clap.interceptor.composite.CompositeInterceptorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.lang.annotation.Annotation;

/**
 * performing transaction and execute LightInterceptors
 *
 * @author Mikhail Demidov
 */
public class ClapMainInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClapMainInterceptor.class);

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private CompositeInterceptorHelper compositeInterceptorHelper;


    @AroundInvoke
    public Object businessIntercept(InvocationContext ctx) throws SystemException {
        Object result = null;
        Annotation[] methodAnnotations = ctx.getMethod().getDeclaredAnnotations();
        if (methodAnnotations.length > 0) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof Transactional) {
                    try {
                        userTransaction.begin();
                        result = compositeInterceptorHelper.execute(ctx);
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
            result = compositeInterceptorHelper.execute(ctx);
        } catch (Exception e) {
            LOGGER.error("error beyond transaction " + e.getMessage(), e);
        }
        return result;
    }

}
