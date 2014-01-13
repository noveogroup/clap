package com.noveogroup.clap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: mdemidov
 * Date: 5/20/13
 * Time: 8:54 AM
 *
 * @since 5/20/13 8:54 AM
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

    String parameterName() default "";

}
