package com.noveogroup.clap.validation;

import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.LightInterceptorQualifier;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrey Sokolov
 */
@LightInterceptorQualifier
@ApplicationScoped
public class ValidationLightInterceptor implements LightInterceptor {

    private LightInterceptor nextInterceptor;
    private ValidatorFactory validatorFactory;

    @PostConstruct
    protected void setup() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @Override
    public void setNextInterceptor(final LightInterceptor nextInterceptor) {
        this.nextInterceptor = nextInterceptor;
    }

    @Override
    public Object proceed(final InvocationContext context, final RequestHelperFactory requestHelperFactory, final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        Object[] parameters = context.getParameters();
        if (parameters != null) {
            Validator validator = validatorFactory.getValidator();
            for (Object parameter : parameters) {
                Set<ConstraintViolation<Object>> violations = validator.validate(parameter);
                if (!violations.isEmpty()) {
                    //TODO somehow exception isn't building message =/
                    throw new ConstraintViolationException(violations);
                }
            }
        }
        return nextInterceptor.proceed(context, requestHelperFactory, annotationMap);
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public String getDescription() {
        return "ValidationLightInterceptor";
    }
}
