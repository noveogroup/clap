package com.noveogroup.clap.interceptor.composite;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public abstract class AbstractLightInterceptor implements LightInterceptor {

    protected LightInterceptor nextInterceptor;

    @Override
    public void setNextInterceptor(final LightInterceptor nextInterceptor) {
        this.nextInterceptor = nextInterceptor;
    }
}
