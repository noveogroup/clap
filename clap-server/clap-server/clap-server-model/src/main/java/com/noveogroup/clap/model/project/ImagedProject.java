package com.noveogroup.clap.model.project;

import com.noveogroup.clap.model.Project;

/**
 * @author Andrey Sokolov
 */
public class ImagedProject extends Project {

    private String iconFileUrl;

    private boolean watched;

    public ImagedProject() {
    }

    /**
     * soft copy constructor
     */
    public ImagedProject(final ImagedProject imagedProject) {
        super(imagedProject);
        this.iconFileUrl = imagedProject.iconFileUrl;
    }

    public String getIconFileUrl() {
        return iconFileUrl;
    }

    public void setIconFileUrl(final String iconFileUrl) {
        this.iconFileUrl = iconFileUrl;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(final boolean watched) {
        this.watched = watched;
    }
}
