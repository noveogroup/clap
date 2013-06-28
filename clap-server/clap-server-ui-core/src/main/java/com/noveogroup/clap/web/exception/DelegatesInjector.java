package com.noveogroup.clap.web.exception;

import com.google.common.collect.Maps;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class DelegatesInjector implements Serializable{

    @Inject
    @Any
    private Instance<ExceptionHandlerDelegate> delegates;

    //private ExceptionHandlerDelegate<ViewExpiredException> viewExpiredExceptionDelegate;

    private final Map<Class,ExceptionHandlerDelegate> delegatesMap = Maps.newHashMap();

    @PostConstruct
    public void init(){
        if(delegates != null){
            for(final ExceptionHandlerDelegate delegate : delegates){
                final Class exceptionClass = delegate.getExceptionClass();
                /*
                if(exceptionClass.equals(ViewExpiredException.class)){
                    viewExpiredExceptionDelegate = delegate;
                    continue;
                } */
                if(delegatesMap.put(exceptionClass,delegate) != null){
                    throw new IllegalStateException("more than one exception handler delegate exists for class " +
                            exceptionClass);
                }
            }    /*
            if(viewExpiredExceptionDelegate == null){
                viewExpiredExceptionDelegate = new DefaultViewExpiredExceptionHandlerDelegate();
            }         */
        }
    }

    public Map<Class, ExceptionHandlerDelegate> getDelegatesMap() {
        return delegatesMap;
    }
            /*
    public ExceptionHandlerDelegate<ViewExpiredException> getViewExpiredExceptionDelegate() {
        return viewExpiredExceptionDelegate;
    }  */
}
