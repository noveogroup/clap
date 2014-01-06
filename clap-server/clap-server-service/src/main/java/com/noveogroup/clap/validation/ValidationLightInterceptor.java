package com.noveogroup.clap.validation;

import com.noveogroup.clap.exception.ClapValidationException;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class ValidationLightInterceptor implements LightInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationLightInterceptor.class);
    private ValidatorFactory validatorFactory;

    @PostConstruct
    protected void setup() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @Override
    public Object proceed(final InvocationContext context,
                          final Stack<LightInterceptor> chain,
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
                    LOGGER.debug("validation failed: "+violations);
                    //TODO somehow exception isn't building message =/
                    throw new ClapValidationException(new ConstraintViolationException(message, violations));
                }
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
        return "ValidationLightInterceptor";
    }
}
