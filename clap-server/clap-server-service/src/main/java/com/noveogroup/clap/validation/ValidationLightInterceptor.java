package com.noveogroup.clap.validation;

import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;

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
    public Object proceed(final InvocationContext context,
                          final RequestHelperFactory requestHelperFactory,
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        final Object[] parameters = context.getParameters();
        if (parameters != null) {
            final Validator validator = validatorFactory.getValidator();
            for (final Object parameter : parameters) {
                final Set<ConstraintViolation<Object>> violations = validator.validate(parameter);
                if (!violations.isEmpty()) {
                    String message = "";
                    for (final ConstraintViolation constraintViolation : violations) {
                        message += " ; " + constraintViolation.getMessage();
                    }
                    //TODO somehow exception isn't building message =/
                    throw new ConstraintViolationException(message, violations);
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
