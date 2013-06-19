package com.noveogroup.clap.interceptor.composite;


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
