package com.noveogroup.clap.interceptor.composite;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Stack;

/**
 * interface to declare light intercepting components
 *
 * introduced to allow introduce low-coupling
 * interceptors without heavy performance impact
 *
 * instances are being invoked in ClapMainInterceptor
 *
 * should be @Singleton or @ApplicationScoped bean
 * should have @LightInterceptorQualifier to be enabled
 *
 * @author Andrey Sokolov
 */
public interface LightInterceptor {

    Object proceed(InvocationContext context,
                   Stack<LightInterceptor> chain,
                   RequestHelperFactory requestHelperFactory,
                   Map<Class<? extends Annotation>,Annotation> annotationMap) throws Exception;

    /**
     * returns priority needed to sort LightInterceptors implementations
     *
     * at value 0 real invocation context invoked
     *
     * @return priority value
     */
    int getPriority();

    /**
     * to log
     *
     * @return short description string
     */
    String getDescription();
}
