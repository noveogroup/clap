package com.noveogroup.clap.web.util.config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.*;
import java.util.Properties;

@Named
@ApplicationScoped
public class ConfigBean {
    // Glassfish Instance Root folder system variable
    private static String glassfishInstanceRootPropertyName = "com.sun.aas.instanceRoot";
    // "config" sub-folder name
    private static String glassfishDomainConfigurationFolderName = "config";


    private long maxApkSize;

    @PostConstruct
    protected void setup() throws IOException {
        Properties properties = new Properties();
        properties.load(readFileFromGlassfishDomainConfigFolder("clap.properties"));
        maxApkSize = Long.parseLong(properties.getProperty("maxApkSize"));
    }

    public long getMaxApkSize() {
        return maxApkSize;
    }

    // Read a given file from Glassfish Domain Configuration folder
    private static InputStream readFileFromGlassfishDomainConfigFolder(final String fileName) throws FileNotFoundException {
        // Instance Root folder
        final String instanceRoot = System.getProperty(glassfishInstanceRootPropertyName);
        if (instanceRoot == null) {
            throw new FileNotFoundException("Cannot find Glassfish instanceRoot. Is the com.sun.aas.instanceRoot system property set?");
        }
        // Instance Root + /config folder
        File configurationFolder = new File(instanceRoot + File.separator + glassfishDomainConfigurationFolderName);
        File configFile = new File(configurationFolder, fileName);
        // return the given file
        return new FileInputStream(configFile);
    }
}
