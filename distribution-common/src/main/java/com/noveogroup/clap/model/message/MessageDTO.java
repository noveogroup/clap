package com.noveogroup.clap.model.message;

import com.noveogroup.clap.model.BaseDTO;

import java.util.Date;

/**
 * @author Mikhail Demidov
 */
public class MessageDTO extends BaseDTO {

    private Date timestamp;

    private String message;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
