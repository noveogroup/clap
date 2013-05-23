package com.noveogroup.clap.interceptor;

import com.noveogroup.clap.interceptor.composite.CompositeInterceptorHelper;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import com.noveogroup.clap.transaction.Transactional;
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


    @Inject
    private CompositeInterceptorHelper compositeInterceptorHelper;

    @Inject
    private RequestHelperFactory requestHelperFactory;

    @AroundInvoke
    public Object businessIntercept(final InvocationContext ctx) throws SystemException {
        try {
            return compositeInterceptorHelper.execute(ctx, requestHelperFactory);
        } catch (Exception e) {
            LOGGER.error("error in intercepted method : ", e);
            return null;
        }
    }

}
