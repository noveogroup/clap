package com.noveogroup.clap.web;

public enum Navigation {
    HOME("index",true),
    REVISION("revision", true),
    PROJECT("project", true),
    PROJECTS("projects", true),
    EDIT_USER("editUser",true),
    USERS_LIST("users",true),
    SAME_PAGE("", false);

    private final boolean facesRedirect;
    private final String view;

    private Navigation(final String view, final boolean facesRedirect) {
        this.view = view;
        this.facesRedirect = facesRedirect;
    }

    public String getView() {
        return view + (facesRedirect ? "?faces-redirect=true" : "");
    }
}
