package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithAuthentication;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationRequestHelper extends RequestHelper {
    UserWithAuthentication getUserRequestData();
    User getUserPersistedData();
    void applyAuthentication(Authentication authentication);
    void onLoginRequired();
    void onLoginFailed();
    void onPermissionMissed();
}
