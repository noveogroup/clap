package com.noveogroup.clap.model.project;

import com.noveogroup.clap.model.Project;

/**
 * @author Andrey Sokolov
 */
public class ImagedProject extends Project {

    private byte[] iconFile;

    public ImagedProject() {
    }

    /**
     * soft copy constructor
     */
    public ImagedProject(final ImagedProject imagedProject) {
        super(imagedProject);
        this.iconFile = imagedProject.iconFile;
    }

    public byte[] getIconFile() {
        return iconFile;
    }

    public void setIconFile(final byte[] iconFile) {
        this.iconFile = iconFile;
    }
}
