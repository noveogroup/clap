package com.noveogroup.clap.config;


import com.google.common.collect.Lists;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Named
@ApplicationScoped
public class ConfigBean {

    private long maxApkSize;

    private String downloadApkUrl;

    private Properties properties;

    private String authenticationSystemId;

    private List<String> tempFilesDirs = Lists.newArrayList();

    private long tempFilesCleanInterval;

    @PostConstruct
    protected void setup() throws IOException {
        properties = ConfigurationUtils.getPropertiesFromConfig("clap.properties");
        maxApkSize = Long.parseLong(properties.getProperty("maxApkSize"));
        downloadApkUrl = properties.getProperty("rest.apkDownload");
        authenticationSystemId = properties.getProperty("authenticationSystemId");
        final String[] tempFilesDirArray = StringUtils.split(properties.getProperty("temp.files.directory"), ';');
        if (ArrayUtils.isNotEmpty(tempFilesDirArray)) {
            for (String tempFilesDir : tempFilesDirArray) {
                checkIfDir(tempFilesDir);
                tempFilesDirs.add(tempFilesDir);
            }
        }
        if (tempFilesDirs.isEmpty()) {
            throw new IllegalArgumentException("No temp files directory is set");
        }
        tempFilesCleanInterval = Long.parseLong(properties.getProperty("temp.files.clean.interval"));
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

    public List<String> getTempFilesDirs() {
        return tempFilesDirs;
    }

    public long getTempFilesCleanInterval() {
        return tempFilesCleanInterval;
    }

    private void checkIfDir(final String dirPath) {
        if (!new File(dirPath).isDirectory()) {
            throw new IllegalArgumentException("Configured " + dirPath + " directory is not directory");
        }
    }
}
