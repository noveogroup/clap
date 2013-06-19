package com.noveogroup.clap.exception;

import com.noveogroup.clap.interceptor.composite.AbstractLightInterceptor;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class ExceptionWrapperLightInterceptor extends AbstractLightInterceptor implements LightInterceptor {

    @Override
    public Object proceed(final InvocationContext context,
                          final RequestHelperFactory requestHelperFactory,
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        try {
            final Object ret = nextInterceptor.proceed(context, requestHelperFactory, annotationMap);
            if(annotationMap.containsKey(WrapException.class)){
                final ExceptionWrapperRequestHelper requestHelper = requestHelperFactory.getRequestHelper(
                        ExceptionWrapperRequestHelper.class);
                requestHelper.getEntityManager().flush();
            }
            return ret;
        } catch (PersistenceException e){
            throw new ClapPersistenceException(e);
        }

    }

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    public String getDescription() {
        return "ExceptionWrapperLightInterceptor";
    }
}
