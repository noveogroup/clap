package com.noveogroup.clap.interceptor.composite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * manages LightInterceptors
 *
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class CompositeInterceptorHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeInterceptorHelper.class);

    @Inject
    @LightInterceptorQualifier
    private Instance<LightInterceptor> lightInterceptors;

    private final List<LightInterceptor> interceptorList = new ArrayList<LightInterceptor>();

    @PostConstruct
    protected void init() {
        if (lightInterceptors != null) {
            for (LightInterceptor interceptor : lightInterceptors) {
                interceptorList.add(interceptor);
                LOGGER.info("registered " + interceptor.getDescription() + " with priority " + interceptor.getPriority());
            }
        }
        int size = interceptorList.size();
        int i = 1;
        if (size > 1) {
            Collections.sort(interceptorList, new Comparator<LightInterceptor>() {
                @Override
                public int compare(LightInterceptor o1, LightInterceptor o2) {
                    //TODO check
                    return o1.getPriority() - o2.getPriority();
                }
            });
            for (; i < size; i++) {
                final LightInterceptor currentInterceptor = interceptorList.get(i - 1);
                currentInterceptor.setNextInterceptor(interceptorList.get(i));
            }
        }
    }

    public Object execute(InvocationContext ctx) throws Exception {
        Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<Class<? extends Annotation>, Annotation>();
        Annotation[] methodAnnotations = ctx.getMethod().getDeclaredAnnotations();
        if (methodAnnotations.length > 0) {
            for (Annotation annotation : methodAnnotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (!annotationMap.containsKey(annotationClass)) {
                    annotationMap.put(annotationClass, annotation);
                } else {
                    LOGGER.error("two annotations of the same class " + annotationClass +
                            " at method " + ctx.getMethod());
                }
            }
        }
        int size = interceptorList.size();
        if (size > 0) {
            ChainedInvocatinoContext chainedInvocatinoContext = new ChainedInvocatinoContext(ctx);
            interceptorList.get(size - 1).setNextInterceptor(chainedInvocatinoContext);
            return interceptorList.get(0).proceed(chainedInvocatinoContext, annotationMap);
        } else {
            return new ChainedInvocatinoContext(ctx).proceed(ctx, annotationMap);
        }
    }

    private final static class ChainedInvocatinoContext implements InvocationContext, LightInterceptor {

        private final InvocationContext wrappedContext;

        private ChainedInvocatinoContext(InvocationContext wrappedContext) {
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
        public void setParameters(Object[] objects) {
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
        public void setNextInterceptor(LightInterceptor nextInterceptor) {
            throw new IllegalArgumentException(nextInterceptor + " has too low priority - " + nextInterceptor.getPriority());
        }

        @Override
        public Object proceed(InvocationContext context, Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
            return wrappedContext.proceed();
        }

        @Override
        public int getPriority() {
            return 9999;
        }

        @Override
        public String getDescription() {
            return "you shouldn't see this";
        }
    }
}
