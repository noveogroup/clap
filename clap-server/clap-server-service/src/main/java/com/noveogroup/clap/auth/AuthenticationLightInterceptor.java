package com.noveogroup.clap.auth;

import com.noveogroup.clap.exception.ClapAuthenticationFailedException;
import com.noveogroup.clap.integration.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.integration.auth.AuthenticationSystem;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Stack;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class AuthenticationLightInterceptor implements LightInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLightInterceptor.class);

    @Inject
    private AuthenticationSystemFactory authenticationSystemFactory;

    private AuthenticationSystem authenticationSystem;

    @PostConstruct
    protected void init(){
        authenticationSystem = authenticationSystemFactory.getAuthenticationSystem();
    }

    @Override
    public Object proceed(final InvocationContext context,
                          final Stack<LightInterceptor> chain,
                          final RequestHelperFactory requestHelperFactory,
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        if(annotationMap.containsKey(AuthenticationRequired.class)){
            final AuthenticationRequestHelper helper = requestHelperFactory
                    .getRequestHelper(AuthenticationRequestHelper.class);
            if(!authenticationSystem.authentifyUser(helper)){
                LOGGER.debug("authentication failed");
                throw new ClapAuthenticationFailedException();
            } else {
                LOGGER.debug("authentication accepted");
            }
        }
        return chain.pop().proceed(context, chain, requestHelperFactory, annotationMap);
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
