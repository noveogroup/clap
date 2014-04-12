package com.noveogroup.clap.web.config;

import com.noveogroup.clap.config.ConfigurationUtils;
import com.noveogroup.clap.rest.exception.ClapException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Andrey Sokolov
 */
@Named
@ApplicationScoped
public class WebConfigBean {

    private boolean enableRegisteringUsers;

    private Properties properties;

    private boolean autoCreateUsers;

    private boolean showThemeSwitcher;

    @PostConstruct
    public void setup() {
        try {
            properties = ConfigurationUtils.getPropertiesFromConfig("clap-ui.properties");
        } catch (IOException e) {
            throw new ClapException(e);
        }
        enableRegisteringUsers = Boolean.parseBoolean(properties.getProperty("enable.registering.users"));
        autoCreateUsers = Boolean.parseBoolean(properties.getProperty("auto.create.users.from.cas"));
        showThemeSwitcher = Boolean.parseBoolean(properties.getProperty("show.theme.switcher"));
    }

    public boolean isAutoCreateUsers() {
        return autoCreateUsers;
    }

    public boolean isEnableRegisteringUsers() {
        return enableRegisteringUsers;
    }

    public boolean isShowThemeSwitcher() {
        return showThemeSwitcher;
    }
}
