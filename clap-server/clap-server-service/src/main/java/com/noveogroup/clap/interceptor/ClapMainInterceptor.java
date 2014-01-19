package com.noveogroup.clap.interceptor;

import com.noveogroup.clap.integration.RequestHelperFactory;
import com.noveogroup.clap.interceptor.composite.CompositeInterceptorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * performing execute LightInterceptors
 *
 * @author Andrey Sokolov
 */
public class ClapMainInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClapMainInterceptor.class);


    @Inject
    private CompositeInterceptorHelper compositeInterceptorHelper;

    @Inject
    private RequestHelperFactory requestHelperFactory;

    @AroundInvoke
    public Object businessIntercept(final InvocationContext ctx) throws Exception {
        return compositeInterceptorHelper.execute(ctx, requestHelperFactory);
    }

}
