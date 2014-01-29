package com.noveogroup.clap.web;

public enum Navigation {
    HOME("/index", true),
    REVISION("/inner/revision/revision", true),
    MY_REVISIONS("/inner/revision/myRevisions", true),
    PROJECT("/inner/project", true),
    PROJECTS("/inner/projects", true),
    EDIT_USER("/inner/editUser", true),
    USERS_LIST("/inner/users", true),
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
