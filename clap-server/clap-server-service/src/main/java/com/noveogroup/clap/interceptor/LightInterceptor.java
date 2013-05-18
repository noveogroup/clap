package com.noveogroup.clap.interceptor;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public interface LightInterceptor {

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
}
