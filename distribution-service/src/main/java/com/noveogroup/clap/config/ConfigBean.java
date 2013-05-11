package com.noveogroup.clap.config;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.*;
import java.util.Properties;

@Named
@ApplicationScoped
public class ConfigBean {

    private long maxApkSize;

    private Properties properties;

    @PostConstruct
    protected void setup() throws IOException {
        properties = ConfigurationUtils.getPropertiesFromConfig("clap.properties");
        maxApkSize = Long.parseLong(properties.getProperty("maxApkSize"));
    }

    public long getMaxApkSize() {
        return maxApkSize;
    }

    public Properties getProperties() {
        return properties;
    }
}
