package com.noveogroup.clap.interceptor;

import com.noveogroup.clap.auth.AuthenticationSystem;
import com.noveogroup.clap.auth.AuthenticationSystemFactory;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;
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
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        if(annotationMap.containsKey(AuthenticationRequired.class)){
            LOGGER.debug("checking auth");
            Object[] parameters = context.getParameters();
            for(Object parameter : parameters){
                if(parameter instanceof Authentication){
                    Authentication authentication = (Authentication) parameter;
                    User user = authenticationSystem.authentifyUser(authentication);
                    if ( user == null){
                        LOGGER.error("auth fail");
                        //throw new AuthenticationException(authentication);
                    }
                }
            }
            //TODO finish method
        }
        return nextInterceptor.proceed(context, annotationMap);
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
