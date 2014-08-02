package com.noveogroup.clap.plugin.config

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
public class HashCalculatorConfig {
    String checksumAlgorithm = "MD5"
    File baseDir
    String[] folders = ['src/**']
    String[] excludes = []


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("checksumAlgorithm", checksumAlgorithm)
                .append("baseDir", baseDir)
                .append("folders", folders)
                .append("excludes", excludes)
                .toString();
    }
}
