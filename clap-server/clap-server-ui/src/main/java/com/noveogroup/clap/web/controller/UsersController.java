package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.user.ResetPasswordModel;
import com.noveogroup.clap.web.model.user.UserSessionData;
import com.noveogroup.clap.web.model.user.UsersListDataModel;
import com.noveogroup.clap.web.model.user.UsersModel;
import com.noveogroup.clap.web.util.message.MessageSupport;
import org.primefaces.event.SelectEvent;
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

    public void prepareUserInfoView() {
        final User user = usersModel.getUser();
        if (user == null) {
            usersModel.setUser(userSessionData.getUser());
        }
    }

    public void prepareUsersListView() {
        final List<User> users = userService.getUsers();
        usersModel.setUsersListDataModel(new UsersListDataModel(users));
    }

    public void saveUser() {
        final User user = usersModel.getUser();
        final User updatedUser = userService.saveUser(user);
        usersModel.setUser(updatedUser);
        messageSupport.addMessage("userInfoMessages", "common.form.info.update.success");
    }

    public void resetPassword() {
        final String newPassword = resetPasswordModel.getNewPassword();
        userService.resetUserPassword(newPassword);
        messageSupport.addMessage("resetPasswordMessages", "common.form.info.update.success");
    }


    public void onUserSelect(final SelectEvent event) {
        final User selectedUser = (User) event.getObject();
        usersModel.setUser(selectedUser);
        LOGGER.debug(selectedUser.getLogin() + " user selected");
        redirectTo(Navigation.EDIT_USER);
    }

}
