package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.user.RequestUserModel;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.model.user.ResetPasswordModel;
import com.noveogroup.clap.web.model.user.UserInfoModel;
import com.noveogroup.clap.web.model.user.UserSessionData;
import com.noveogroup.clap.web.util.message.MessageSupport;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
@RequestScoped
public class UserInfoController {

    @Inject
    private UserInfoModel userInfoModel;

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private UserService userService;

    @Inject
    private ResetPasswordModel resetPasswordModel;

    @Inject
    private MessageSupport messageSupport;

    public void prepareUserInfoView() {
        final String login = userSessionData.getUser().getLogin();
        final User user = userService.getUser(login);
        userInfoModel.setUser(user);
    }

    public void saveUser() {
        final User user = userInfoModel.getUser();
        final User updatedUser = userService.saveUser(user);
        userInfoModel.setUser(updatedUser);
        messageSupport.addMessage("userInfoMessages", "common.form.info.update.success");
    }

    public void resetPassword() {
        final String login = userSessionData.getUser().getLogin();
        final String password = resetPasswordModel.getOldPassword();
        final String newPassword = resetPasswordModel.getNewPassword();
        final RequestUserModel requestUserModel = new RequestUserModel();
        requestUserModel.setLogin(login);
        requestUserModel.setPassword(password);
        userService.resetUserPassword(requestUserModel, newPassword);
        userSessionData.getUser().setPassword(newPassword);
        messageSupport.addMessage("resetPasswordMessages", "common.form.info.update.success");
    }

}
