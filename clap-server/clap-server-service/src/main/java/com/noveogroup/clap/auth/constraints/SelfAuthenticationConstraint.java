package com.noveogroup.clap.auth.constraints;

import com.noveogroup.clap.auth.AuthenticationConstraint;
import com.noveogroup.clap.model.auth.ConstraintViolation;
import com.noveogroup.clap.model.user.Role;
import com.noveogroup.clap.model.user.User;
import org.apache.commons.lang3.StringUtils;


/**
 * @author Andrey Sokolov
 */
public class SelfAuthenticationConstraint implements AuthenticationConstraint {

    private User subject;

    public SelfAuthenticationConstraint(User subject) {
        this.subject = subject;
    }

    @Override
    public ConstraintViolation isSatisfy(User user) {
        if(Role.ADMIN.equals(user.getRole()) || StringUtils.equals(subject.getLogin(),user.getLogin())){
            return null;
        } else {
            return new Violation();
        }
    }

    public static class Violation implements ConstraintViolation{
        @Override
        public String getDescription() {
            return "Only ADMIN and user himself can modify user";
        }
    }
}
