package com.noveogroup.clap.model;

/**
 * @author Mikhail Demidov
 */
public class BaseModel {

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
