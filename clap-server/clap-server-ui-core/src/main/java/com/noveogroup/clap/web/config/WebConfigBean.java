package com.noveogroup.clap.web.config;

import com.noveogroup.clap.config.ConfigurationUtils;

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

    @PostConstruct
    public void setup() throws IOException {
        properties = ConfigurationUtils.getPropertiesFromConfig("clap-ui.properties");
        enableRegisteringUsers = Boolean.parseBoolean(properties.getProperty("enable.registering.users"));
    }

    public boolean isEnableRegisteringUsers() {
        return enableRegisteringUsers;
    }
}
