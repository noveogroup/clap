package com.noveogroup.clap.model;

/**
 * @author Mikhail Demidov
 */
public class BaseDTO {

    private Long id;

    /**
     * Constructor
     */
    public BaseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
