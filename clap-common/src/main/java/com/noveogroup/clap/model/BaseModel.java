package com.noveogroup.clap.model;

/**
 * @author Mikhail Demidov
 */
public class BaseModel {

    private Long id;

    /**
     * Constructor
     */
    public BaseModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
