package com.noveogroup.clap.integration.auth;

import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Andrey Sokolov
 */
public class DefaultAuthenticationSystemTest {
    private DefaultAuthenticationSystem system = new DefaultAuthenticationSystem();

    @Test
    public void testCheckAuth() throws Exception {
        RequestUserModel requestUer = new RequestUserModel();
        requestUer.setPassword("123");
        UserWithPersistedAuth persistedUser = new UserWithPersistedAuth();
        persistedUser.setAuthenticationKey("202cb962ac59075b964b07152d234b70");
        assertTrue(system.checkAuth(requestUer, persistedUser));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAuth2() throws Exception {
        system.checkAuth(new RequestUserModel(), new UserWithPersistedAuth());
    }
}
