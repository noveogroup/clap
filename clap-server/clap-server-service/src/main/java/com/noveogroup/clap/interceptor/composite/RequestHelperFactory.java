package com.noveogroup.clap.interceptor.composite;

import com.noveogroup.clap.integration.RequestHelper;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public class RequestHelperFactory {

    @Inject
    @Any
    private Instance<RequestHelper> requestHelpers;

    private final Map<Class<? extends RequestHelper>,RequestHelper> requestHelperMap = new HashMap<Class<? extends RequestHelper>, RequestHelper>();


    @PostConstruct
    protected void init(){
        for(final RequestHelper helper:requestHelpers){
            final Class<? extends RequestHelper> key = helper.getType();
            if(requestHelperMap.containsKey(key)){
                throw new IllegalStateException("multiple request helpers for key type : " + key);
            }
            requestHelperMap.put(key, helper);
        }
    }

    public <T extends RequestHelper> T getRequestHelper(final Class<? extends T> requestHelperClass){
        return (T) requestHelperMap.get(requestHelperClass);
    }
}
