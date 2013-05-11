package com.noveogroup.clap.web;

public enum Navigation {
    PROJECT("project"),
    PROJECTS("projects");

    private Navigation(String view) {
        this.view = view;
    }

    private String view;

    public String getView() {
        return view;
    }
}
