package com.noveogroup.clap.model;

import java.io.Serializable;

/**
 * @author Mikhail Demidov
 */
public class BaseModel implements Serializable{

    private Long id;

    public BaseModel() {
    }

    /**
     * soft copy constructor
     */
    public BaseModel(final BaseModel baseModel) {
        this.id = baseModel.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
