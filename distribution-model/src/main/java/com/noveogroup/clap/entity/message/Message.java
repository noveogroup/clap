package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: admin2
 * Date: 5/11/13
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "messages")
public class Message extends BaseEntity {


    private Long timestamp;

    private String message;

    /**
     * Constructor
     */
    public Message() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
