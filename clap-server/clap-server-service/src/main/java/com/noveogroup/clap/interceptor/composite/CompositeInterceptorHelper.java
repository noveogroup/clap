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
                    return o2.getPriority() - o1.getPriority();
                }
            });
            for (; i < size; i++) {
                final LightInterceptor currentInterceptor = interceptorList.get(i - 1);
                currentInterceptor.setNextInterceptor(interceptorList.get(i));
            }
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
            final ChainedInvocationContext chainedInvocationContext = new ChainedInvocationContext(ctx);
            interceptorList.get(size - 1).setNextInterceptor(chainedInvocationContext);
            return interceptorList.get(0).proceed(chainedInvocationContext, requestHelperFactory,annotationMap);
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
        public void setNextInterceptor(final LightInterceptor nextInterceptor) {
            throw new IllegalStateException("this metod shoudn't be invoked");
        }

        @Override
        public Object proceed(final InvocationContext context,
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
            throw new IllegalStateException("this metod shoudn't be invoked");
        }
    }
}
