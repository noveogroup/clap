package com.noveogroup.clap.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    private static final long serialVersionUID = 8306757495649843962L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
