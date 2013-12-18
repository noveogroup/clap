package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.integration.RequestHelper;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;

/**
 * @author Andrey Sokolov
 */
public interface AuthenticationRequestHelper extends RequestHelper {
    RequestUserModel getUserRequestData();
    UserWithPersistedAuth getUserPersistedData();
    void applyAuthentication(Authentication authentication);
    void onLoginRequired();
    void onLoginFailed();
    void onPermissionMissed();
}
