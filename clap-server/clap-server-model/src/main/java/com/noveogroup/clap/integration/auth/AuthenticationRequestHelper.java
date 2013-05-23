package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationRequestHelper extends RequestHelper {
    User getUserData();
    void applyAuthentication(Authentication authentication);
    void storeUserSessionData(User user);
    void onLoginRequired();
    void onLoginFailed();
    void onPermissionMissed();
}
