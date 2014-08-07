package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.ResetPasswordModel;
import com.noveogroup.clap.web.model.user.UserSessionData;
import com.noveogroup.clap.web.model.user.UsersListDataModel;
import com.noveogroup.clap.web.model.user.UsersModel;
import com.noveogroup.clap.web.util.message.MessageSupport;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class UsersController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Inject
    private UsersModel usersModel;

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private UserService userService;

    @Inject
    private ResetPasswordModel resetPasswordModel;

    @Inject
    private MessageSupport messageSupport;

    public void prepareMyUserView() {
        usersModel.setUser(userSessionData.getUser());
    }


    public void prepareUsersListView() {
        final List<User> users = userService.getUsers();
        usersModel.setUsersListDataModel(new UsersListDataModel(users));
    }

    public void saveUser() {
        final User user = usersModel.getUser();
        final User updatedUser = userService.saveUser(user);
        postSuccessMessage(updatedUser, "userInfoMessages");
    }

    public void resetPassword() {
        final String newPassword = resetPasswordModel.getNewPassword();
        userService.resetUserPassword(newPassword);
        postSuccessMessage("resetPasswordMessages");
    }

    public void editPermissions() {
        final User user = usersModel.getUser();
        DualListModel<ClapPermission> userPermissions = usersModel.getUserPermissions();
        if (user != null && userPermissions != null) {
            user.setClapPermissions(userPermissions.getTarget());
            final User updatedUser = userService.editPermissions(user);
            postSuccessMessage(updatedUser, "editPermissionsMessages");
        }
    }

    public void editRole() {
        final User updatedUser = userService.editRole(usersModel.getUser());
        postSuccessMessage(updatedUser, "editRoleMessages");
    }

    private void postSuccessMessage(final String componentId) {
        postSuccessMessage(null, componentId);
    }

    private void postSuccessMessage(final User updatedUser, final String componentId) {
        if (updatedUser != null) {
            usersModel.setUser(updatedUser);
        }
        messageSupport.addMessage(componentId, "common.form.info.update.success");
    }


    public void onUserSelect(final SelectEvent event) {
        final User selectedUser = (User) event.getObject();
        usersModel.setUser(selectedUser);
        LOGGER.debug(selectedUser.getLogin() + " user selected");
        redirectTo(Navigation.USER_INFO);
    }

    public void editUser(final String login){
        final User user = usersModel.getUsersListDataModel().getRowData(login);
        usersModel.setUser(user);
        redirectTo(Navigation.EDIT_USER);
    }
}
