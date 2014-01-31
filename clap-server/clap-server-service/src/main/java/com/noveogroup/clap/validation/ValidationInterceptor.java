package com.noveogroup.clap.validation;

import com.noveogroup.clap.exception.ClapValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Andrey Sokolov
 */
@Validate
@Interceptor
public class ValidationInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationInterceptor.class);
    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    @AroundInvoke
    public Object proceed(final InvocationContext context) throws Exception {
        final Class<?> targetClass = context.getTarget().getClass();
        final Method method = context.getMethod();
        //to exclude implicitly enabled EJBs
        if (targetClass.isAnnotationPresent(Validate.class) || method.isAnnotationPresent(Validate.class)) {
            final Object[] parameters = context.getParameters();
            if (parameters != null && parameters.length > 0) {
                final Validator validator = validatorFactory.getValidator();
                for (final Object parameter : parameters) {
                    final Set<ConstraintViolation<Object>> violations = validator.validate(parameter);
                    if (!violations.isEmpty()) {
                        String message = "";
                        for (final ConstraintViolation constraintViolation : violations) {
                            message += " ; " + constraintViolation.getMessage();
                        }
                        LOGGER.debug("validation failed: " + violations);
                        //TODO somehow exception isn't building message =/
                        throw new ClapValidationException(new ConstraintViolationException(message, violations));
                    }
                }
            }
        }
        return context.proceed();
    }
}
