package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.model.auth.ConstraintViolation;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class AuthConstraintViolationException extends ClapException {
    private final List<ConstraintViolation> violations;

    public AuthConstraintViolationException(final List<ConstraintViolation> violations) {
        this.violations = violations;
    }
}
