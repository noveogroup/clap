package com.noveogroup.clap.interceptor.composite;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.Map;

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

    /**
     * you should store this instance and invoke it's proceed() method during
     * execution of your proceed() instead of InvocationContext#proceed()
     *
     * @param nextInterceptor
     */
    void setNextInterceptor(LightInterceptor nextInterceptor);

    Object proceed(InvocationContext context, Map<Class<? extends Annotation>,Annotation> annotationMap) throws Exception;

    /**
     * returns priority needed to sort LightInterceptors implementations
     *
     * at value 9999 real invocation context invoked
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
