package com.noveogroup.clap.auth;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationSystem {
    User authentifyUser(Authentication authentication);
    String getSystemId();
}
