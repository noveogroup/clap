package com.noveogroup.clap.auth;


import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationConstraint {
    /**
     * check if user pass constraint
     *
     * @param user
     * @return null if no violation
     */
    ConstraintViolation isSatisfy(User user);
}
