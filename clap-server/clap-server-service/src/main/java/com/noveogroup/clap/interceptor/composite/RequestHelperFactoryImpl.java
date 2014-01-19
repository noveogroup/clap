package com.noveogroup.clap.interceptor.composite;

import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.integration.RequestHelperFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class RequestHelperFactoryImpl implements RequestHelperFactory{

    @Inject
    @Any
    private Instance<RequestHelper> requestHelpers;

    private final Map<Class<? extends RequestHelper>,RequestHelper> requestHelperMap
            = new HashMap<Class<? extends RequestHelper>, RequestHelper>();


    @PostConstruct
    protected void init(){
        for(final RequestHelper helper:requestHelpers){
            addRequestHelper(helper);
        }
    }

    public <T extends RequestHelper> T getRequestHelper(final Class<? extends T> requestHelperClass){
        return (T) requestHelperMap.get(requestHelperClass);
    }

    @Override
    public void addRequestHelper(final RequestHelper requestHelper) {
        final Class<? extends RequestHelper> key = requestHelper.getType();
        if(requestHelperMap.containsKey(key)){
            throw new IllegalStateException("multiple request helpers for key type : " + key);
        }
        requestHelperMap.put(key, requestHelper);
    }
}
