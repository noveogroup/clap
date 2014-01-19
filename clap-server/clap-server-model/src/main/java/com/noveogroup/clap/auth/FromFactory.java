package com.noveogroup.clap.auth;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @author Andrey Sokolov
 */
@Qualifier
@Target({TYPE,METHOD,PARAMETER,FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FromFactory {
}
