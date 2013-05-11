package com.noveogroup.clap.model;

public class ProjectDTO {

    private Long id;

    private String name;

    private String description;

    private Long creationDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Long creationDate) {
        this.creationDate = creationDate;
    }
}
