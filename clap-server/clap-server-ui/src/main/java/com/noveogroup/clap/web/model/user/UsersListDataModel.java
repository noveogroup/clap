package com.noveogroup.clap.web.model.user;

import com.noveogroup.clap.model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class UsersListDataModel extends ListDataModel<User> implements SelectableDataModel<User> {

    public UsersListDataModel(final List<User> list) {
        super(list);
    }

    @Override
    public Object getRowKey(final User user) {
        return user.getLogin();
    }

    @Override
    public User getRowData(final String s) {
        for(User user : (List<User>)getWrappedData()){
            if(StringUtils.equals(s,user.getLogin())){
                return user;
            }
        }
        return null;
    }
}
