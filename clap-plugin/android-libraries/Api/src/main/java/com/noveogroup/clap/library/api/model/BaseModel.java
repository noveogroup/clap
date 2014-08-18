package com.noveogroup.clap.library.api.model;

public class BaseModel {
    private Long id;

    public BaseModel() {
    }

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
