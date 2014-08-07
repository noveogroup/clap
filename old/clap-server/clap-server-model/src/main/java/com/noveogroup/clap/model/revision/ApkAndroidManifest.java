package com.noveogroup.clap.model.revision;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andrey Sokolov
 */
//TODO add fields
public class ApkAndroidManifest {

    private String iconPath;

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(final String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("iconPath", iconPath).
                toString();
    }
}
