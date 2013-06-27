package com.noveogroup.clap.interceptor.composite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * manages LightInterceptors
 *
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class CompositeInterceptorHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeInterceptorHelper.class);

    @Inject
    @Any
    private Instance<LightInterceptor> lightInterceptors;

    private final List<LightInterceptor> interceptorList = new ArrayList<LightInterceptor>();

    @PostConstruct
    protected void init() {
        if (lightInterceptors != null) {
            for (final LightInterceptor interceptor : lightInterceptors) {
                interceptorList.add(interceptor);
                LOGGER.info("registered " + interceptor.getDescription()
                        + " with priority " + interceptor.getPriority());
            }
        }
        final int size = interceptorList.size();
        int i = 1;
        if (size > 1) {
            Collections.sort(interceptorList, new Comparator<LightInterceptor>() {
                @Override
                public int compare(final LightInterceptor o1, final LightInterceptor o2) {
                    return o1.getPriority() - o2.getPriority();
                }
            });
        }
    }

    public Object execute(final InvocationContext ctx,
                          final RequestHelperFactory requestHelperFactory) throws Exception {
        final Map<Class<? extends Annotation>, Annotation> annotationMap
                = new HashMap<Class<? extends Annotation>, Annotation>();
        final Annotation[] methodAnnotations = ctx.getMethod().getDeclaredAnnotations();
        if (methodAnnotations.length > 0) {
            for (final Annotation annotation : methodAnnotations) {
                final Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (!annotationMap.containsKey(annotationClass)) {
                    annotationMap.put(annotationClass, annotation);
                } else {
                    LOGGER.error("two annotations of the same class " + annotationClass +
                            " at method " + ctx.getMethod());
                }
            }
        }
        final int size = interceptorList.size();
        if (size > 0) {
            final Stack<LightInterceptor> chain = new Stack<LightInterceptor>();
            final ChainedInvocationContext chainedInvocationContext = new ChainedInvocationContext(ctx);
            chain.push(chainedInvocationContext);
            for (LightInterceptor interceptor : interceptorList){
                chain.push(interceptor);
            }
            return chain.pop().proceed(chainedInvocationContext, chain, requestHelperFactory, annotationMap);
        } else {
            return ctx.proceed();
        }
    }

    private final static class ChainedInvocationContext implements InvocationContext, LightInterceptor {

        private final InvocationContext wrappedContext;

        private ChainedInvocationContext(final InvocationContext wrappedContext) {
            this.wrappedContext = wrappedContext;
        }

        @Override
        public Object getTarget() {
            return wrappedContext.getTarget();
        }

        @Override
        public Object getTimer() {
            return wrappedContext.getTimer();
        }

        @Override
        public Method getMethod() {
            return wrappedContext.getMethod();
        }

        @Override
        public Object[] getParameters() {
            return wrappedContext.getParameters();
        }

        @Override
        public void setParameters(final Object[] objects) {
            wrappedContext.setParameters(objects);
        }

        @Override
        public Map<String, Object> getContextData() {
            return wrappedContext.getContextData();
        }

        @Override
        public Object proceed() throws Exception {
            throw new IllegalAccessException("invoke nextInterceptor instead");
        }

        @Override
        public Object proceed(final InvocationContext context,
                              final Stack<LightInterceptor> chain,
                              final RequestHelperFactory requestHelperFactory,
                              final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
            return wrappedContext.proceed();
        }

        @Override
        public int getPriority() {
            throw new IllegalStateException("this metod shoudn't be invoked");
        }

        @Override
        public String getDescription() {
            return "chained invocation context";
        }
    }
}
