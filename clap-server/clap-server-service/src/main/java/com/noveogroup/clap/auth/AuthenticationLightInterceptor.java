package com.noveogroup.clap.auth;

import com.google.common.collect.Lists;
import com.noveogroup.clap.auth.constraints.InRole;
import com.noveogroup.clap.auth.constraints.InRoleAuthenticationConstraint;
import com.noveogroup.clap.auth.constraints.Self;
import com.noveogroup.clap.auth.constraints.SelfAuthenticationConstraint;
import com.noveogroup.clap.integration.RequestHelperFactory;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.List;
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
            authenticationSystem.authentifyUser(helper,getAuthConstraints(context,annotationMap));
            LOGGER.debug("authentication accepted");
        }
        return chain.pop().proceed(context, chain, requestHelperFactory, annotationMap);
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public String getDescription() {
        return "AuthenticationLightInterceptor";
    }

    private List<AuthenticationConstraint> getAuthConstraints(final InvocationContext context,
            final Map<Class<? extends Annotation>, Annotation> annotationMap){
        final List<AuthenticationConstraint> ret = Lists.newArrayList();
        final Annotation[][] parameterAnnotations = context.getMethod().getParameterAnnotations();
        for (int i = 0; i< parameterAnnotations.length; i++){
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations){
                checkSelfAnnotation(context, ret, i, annotation);
                checkInRoleAnnotation(annotationMap,ret);
                //TODO another constaints
            }
        }
        return ret;
    }

    private void checkSelfAnnotation(final InvocationContext context, final List<AuthenticationConstraint> ret,
                                     final int i, final Annotation annotation) {
        if(annotation instanceof Self){
            final Object o = context.getParameters()[i];
            if(o instanceof User){
                ret.add(new SelfAuthenticationConstraint((User) o));
            } else {
                throw new IllegalArgumentException("@Self parameter should be instance of User class");
            }
        }
    }

    private void checkInRoleAnnotation(final Map<Class<? extends Annotation>, Annotation> annotationMap,
                                       final List<AuthenticationConstraint> ret){
        if(annotationMap.containsKey(InRole.class)){
            final InRole annotation = (InRole)annotationMap.get(InRole.class);
            ret.add(new InRoleAuthenticationConstraint(annotation.role()));
        }
    }
}
