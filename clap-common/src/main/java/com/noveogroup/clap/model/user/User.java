package com.noveogroup.clap.model.user;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrey Sokolov
 */
@XmlRootElement
public class User extends BaseUser {
    private String fullName;


    @XmlElement
    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("fullName='").append(fullName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
