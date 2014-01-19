package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.user.UserCreationModel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
@Named
@SessionScoped
public class RegisterUserModel implements Serializable{

    private UserCreationModel user = new UserCreationModel();

    public UserCreationModel getUser() {
        return user;
    }
}
