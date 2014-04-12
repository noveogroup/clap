package com.noveogroup.clap.entity.message;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Andrey Sokolov
 */
@Entity(name = "ScreenshotMessageEntity")
@DiscriminatorValue("screenshot")
public class ScreenshotMessageEntity extends BaseMessageEntity {

    @Column(name = "screenshot",nullable = true)
    private String screenshotFileUrl;

    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return ScreenshotMessageEntity.class;
    }

    public String getScreenshotFileUrl() {
        return screenshotFileUrl;
    }

    public void setScreenshotFileUrl(final String screenshotFileUrl) {
        this.screenshotFileUrl = screenshotFileUrl;
    }
}
