package com.noveogroup.clap.entity.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;

/**
 * @author Andrey Sokolov
 */
@Entity(name = "ScreenshotMessageEntity")
@Table(name = "screenshotMessages")
public class ScreenshotMessageEntity extends BaseMessageEntity {

    @Column(name = "screenshot",nullable = true)
    @Lob
    private Blob screenshot;

    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return ScreenshotMessageEntity.class;
    }

    public Blob getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(final Blob screenshot) {
        this.screenshot = screenshot;
    }
}
