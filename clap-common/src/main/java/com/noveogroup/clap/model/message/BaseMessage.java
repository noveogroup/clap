package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.BaseModel;

import java.util.Date;

/**
 * @author Andrey Sokolov
 */
public class BaseMessage extends BaseModel {
    protected Date timestamp;
    private String uploadedBy;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
