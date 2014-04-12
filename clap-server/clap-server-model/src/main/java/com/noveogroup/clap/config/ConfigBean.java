package com.noveogroup.clap.config;


import com.google.common.collect.Lists;
import com.noveogroup.clap.rest.exception.ClapException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigBean.class);

    private static final String CONFIG_FILE_NAME = "clap.properties";

    private long maxApkSize;

    private String downloadApkUrl;

    private Properties properties;

    private String authenticationSystemId;

    private List<String> apkFilesDirs = Lists.newArrayList();

    private List<String> screenshotFilesDirs = Lists.newArrayList();

    private List<String> tempFilesDirs = Lists.newArrayList();

    private long tempFilesCleanInterval;

    private long updateConfigInterval;

    private long configFileLastModified;

    private int keepDevRevisions;

    @PostConstruct
    protected void setup() {
        try {
            properties = ConfigurationUtils.getPropertiesFromConfig(CONFIG_FILE_NAME);
        } catch (IOException e) {
            throw new ClapException(e);
        }
        maxApkSize = Long.parseLong(properties.getProperty("maxApkSize"));
        downloadApkUrl = properties.getProperty("rest.apkDownload");
        authenticationSystemId = properties.getProperty("authenticationSystemId");
        fillStringArrayConfig("files.directory.apk", apkFilesDirs);
        fillStringArrayConfig("files.directory.screenshots", screenshotFilesDirs);
        fillStringArrayConfig("files.directory.temp", tempFilesDirs);
        tempFilesCleanInterval = Long.parseLong(properties.getProperty("temp.files.clean.interval"));
        updateConfigInterval = Long.parseLong(properties.getProperty("config.update.interval"));
        keepDevRevisions = Integer.parseInt(properties.getProperty("keep.dev.revisions"));
        configFileLastModified = getConfigFileLastModified();
    }

    private void fillStringArrayConfig(final String property, final List<String> configList) {
        final String[] tempFilesDirArray = StringUtils.split(properties.getProperty(property), ';');
        if (ArrayUtils.isNotEmpty(tempFilesDirArray)) {
            for (String tempFilesDir : tempFilesDirArray) {
                checkIfDir(tempFilesDir);
                configList.add(tempFilesDir);
            }
        }
        if (configList.isEmpty()) {
            throw new IllegalArgumentException("No files directory is set for" + property);
        }
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

    public long getUpdateConfigInterval() {
        return updateConfigInterval;
    }

    private void checkIfDir(final String dirPath) {
        if (!new File(dirPath).isDirectory()) {
            throw new IllegalArgumentException("Configured " + dirPath + " directory is not directory");
        }
    }

    public synchronized void updateConfig() throws IOException {
        if (isFileChanged()) {
            setup();
            LOGGER.info("Config updated: " + toString());
        }
    }

    private boolean isFileChanged() {
        return configFileLastModified < getConfigFileLastModified();
    }

    private long getConfigFileLastModified() {
        final File configFile = new File(CONFIG_FILE_NAME);
        return configFile.lastModified();
    }

    public int getKeepDevRevisions() {
        return keepDevRevisions;
    }

    public List<String> getScreenshotFilesDirs() {
        return screenshotFilesDirs;
    }

    public List<String> getApkFilesDirs() {
        return apkFilesDirs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("maxApkSize", maxApkSize)
                .append("downloadApkUrl", downloadApkUrl)
                .append("authenticationSystemId", authenticationSystemId)
                .append("tempFilesDirs", tempFilesDirs)
                .append("tempFilesCleanInterval", tempFilesCleanInterval)
                .append("updateConfigInterval", updateConfigInterval)
                .append("configFileLastModified", configFileLastModified)
                .append("keepDevRevisions", keepDevRevisions)
                .toString();
    }
}
