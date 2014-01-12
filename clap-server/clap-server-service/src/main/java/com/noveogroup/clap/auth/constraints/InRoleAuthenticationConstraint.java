package com.noveogroup.clap.auth.constraints;

import com.noveogroup.clap.integration.auth.AuthenticationConstraint;
import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.Role;
import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public class InRoleAuthenticationConstraint implements AuthenticationConstraint {

    private Role role;

    public InRoleAuthenticationConstraint(final Role role) {
        this.role = role;
    }

    @Override
    public ConstraintViolation isSatisfy(final User user) {
        //TODO make <=
        if(role.equals(user.getRole())){
            return null;
        } else {
            return new Violation(role);
        }
    }


    public static class Violation implements ConstraintViolation{

        private Role role;

        public Violation(final Role role) {
            this.role = role;
        }

        @Override
        public String getDescription() {
            return "This only available for " + role + " user";
        }
    }
}
