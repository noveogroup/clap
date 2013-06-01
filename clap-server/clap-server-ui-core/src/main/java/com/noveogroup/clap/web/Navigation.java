package com.noveogroup.clap.web;

public enum Navigation {
    REVISION("revision", true),
    PROJECT("project", true),
    PROJECTS("projects", true),
    DEFAULT_LOGIN_PAGE("auth/default_login",true),
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
