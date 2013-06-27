package com.noveogroup.clap.exception;

import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Stack;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class ExceptionWrapperLightInterceptor implements LightInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionWrapperLightInterceptor.class);

    @Override
    public Object proceed(final InvocationContext context,
                          final Stack<LightInterceptor> chain,
                          final RequestHelperFactory requestHelperFactory,
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        try {
            final Object ret = chain.pop().proceed(context, chain, requestHelperFactory, annotationMap);
            if(annotationMap.containsKey(WrapException.class)){
                final ExceptionWrapperRequestHelper requestHelper = requestHelperFactory.getRequestHelper(
                        ExceptionWrapperRequestHelper.class);
                requestHelper.getEntityManager().flush();
            }
            return ret;
        } catch (PersistenceException e){
            LOGGER.debug("exception wrapped : " + e);
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
