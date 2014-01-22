package com.noveogroup.clap.config;

import java.util.Properties;

/**
 * Extension to Properties to allow for a value to contain a variable reference to another property.
 */
public class VariablableProperties extends Properties {

    private static final long serialVersionUID = 1L;

    private static final String START_DELIMITER = "${";
    private static final String END_DELIMITER = "}";

    @Override
    public String getProperty(final String key) {
        String value = super.getProperty(key);

        if (value != null) {
            int startIndex = 0;
            int endIndex = 0;
            while ((startIndex = value.indexOf(START_DELIMITER, endIndex)) >= 0
                    && (endIndex = value.indexOf(END_DELIMITER, startIndex)) >= 0) {

                final String variableName = value.substring(startIndex + START_DELIMITER.length(), endIndex);
                // now call getProperty recursively to have this looked up
                String variableValue = null;

                if (!variableName.equals(key)) {
                    // only recurse if the variable does not equal our own original  key
                    variableValue = this.getProperty(variableName);
                }

                if (variableValue == null) {
                    // when unable to find the variable value, just return it as the variable name
                    variableValue = START_DELIMITER + variableName + END_DELIMITER;
                }
                value = value.replace(START_DELIMITER + variableName + END_DELIMITER, variableValue);
            } // while matches.
        }
        return value;
    }
}