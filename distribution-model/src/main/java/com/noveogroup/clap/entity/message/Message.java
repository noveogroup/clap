package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

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


    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private String message;

    /**
     * Constructor
     */
    public Message() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
