package com.noveogroup.clap.web.model;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.user.Role;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class CommonModels {

    private final List<SelectItem> roles = Lists.newArrayList();

    @PostConstruct
    public void setup(){
        for(Role role : Role.values()){
            roles.add(new SelectItem(role,role.name()));
        }
    }

    public List<SelectItem> getRoles(){
        return roles;
    }

}
