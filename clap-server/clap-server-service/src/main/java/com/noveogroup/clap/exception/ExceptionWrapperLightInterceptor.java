package com.noveogroup.clap.exception;

import com.noveogroup.clap.interceptor.composite.AbstractLightInterceptor;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import com.noveogroup.clap.transaction.Transactional;

import javax.ejb.Stateless;
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
    public Object proceed(InvocationContext context, RequestHelperFactory requestHelperFactory, Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        try {
            Object ret = nextInterceptor.proceed(context, requestHelperFactory, annotationMap);
            if(annotationMap.containsKey(Transactional.class)){
                ExceptionWrapperRequestHelper requestHelper = requestHelperFactory.getRequestHelper(ExceptionWrapperRequestHelper.class);
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
