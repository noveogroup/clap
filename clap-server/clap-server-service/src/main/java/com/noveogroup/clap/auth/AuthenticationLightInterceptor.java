package com.noveogroup.clap.auth;

import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.integration.auth.AuthenticationSystem;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.LightInterceptorQualifier;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@LightInterceptorQualifier
@ApplicationScoped
public class AuthenticationLightInterceptor implements LightInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLightInterceptor.class);

    private LightInterceptor nextInterceptor;

    @Inject
    private AuthenticationSystemFactory authenticationSystemFactory;

    private AuthenticationSystem authenticationSystem;

    @PostConstruct
    protected void init(){
        authenticationSystem = authenticationSystemFactory.getAuthenticationSystem();
    }


    @Override
    public void setNextInterceptor(final LightInterceptor nextInterceptor) {
        this.nextInterceptor = nextInterceptor;
    }

    @Override
    public Object proceed(final InvocationContext context,
                          final RequestHelperFactory requestHelperFactory,
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        if(annotationMap.containsKey(AuthenticationRequired.class)){
            final AuthenticationRequestHelper helper = requestHelperFactory.getRequestHelper(AuthenticationRequestHelper.class);
            if(!authenticationSystem.authentifyUser(helper)){
                return null;
            }
        }
        return nextInterceptor.proceed(context,requestHelperFactory, annotationMap);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public String getDescription() {
        return "AuthenticationLightInterceptor";
    }
}
