package com.noveogroup.clap.config;

import java.io.*;
import java.util.Properties;

public final class ConfigurationUtils {
    // Glassfish Instance Root folder system variable
    private static String glassfishInstanceRootPropertyName = "com.sun.aas.instanceRoot";
    // "config" sub-folder name
    private static String glassfishDomainConfigurationFolderName = "config";

    private ConfigurationUtils() {
        throw new UnsupportedOperationException("instantiate util class");
    }

    public static Properties getPropertiesFromConfig(final String configFileName) throws IOException {
        Properties properties = new VariablableProperties();
        properties.load(readFileFromGlassfishDomainConfigFolder(configFileName));
        return properties;
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
