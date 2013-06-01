package com.noveogroup.clap.config;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.Properties;

@Named
@ApplicationScoped
public class ConfigBean {

    private long maxApkSize;

    private String downloadApkUrl;

    private Properties properties;

    private String authenticationSystemId;

    private String persistenceUnitName;

    @PostConstruct
    protected void setup() throws IOException {
        properties = ConfigurationUtils.getPropertiesFromConfig("clap.properties");
        maxApkSize = Long.parseLong(properties.getProperty("maxApkSize"));
        downloadApkUrl = properties.getProperty("rest.apkDownload");
        authenticationSystemId = properties.getProperty("authenticationSystemId");
        persistenceUnitName = properties.getProperty("persistence.context");
    }

    public long getMaxApkSize() {
        return maxApkSize;
    }

    public String getDownloadApkUrl() {
        return downloadApkUrl;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getAuthenticationSystemId() {
        return authenticationSystemId;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }
}
