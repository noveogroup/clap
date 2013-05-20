package com.noveogroup.clap.web;

public enum Navigation {
    REVISION("revision", true),
    PROJECT("project", true),
    PROJECTS("projects", true),
    SAME_PAGE("", false);

    private Navigation(String view, boolean facesRedirect) {
        this.view = view;
        this.facesRedirect = facesRedirect;
    }

    private final boolean facesRedirect;
    private final String view;

    public String getView() {
        return view + (facesRedirect ? "?faces-redirect=true" : "");
    }
}
