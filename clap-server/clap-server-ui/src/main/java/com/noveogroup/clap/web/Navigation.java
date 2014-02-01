package com.noveogroup.clap.web;

public enum Navigation {
    HOME("/index", true),
    REVISION("/inner/revision/revision", true),
    MY_REVISIONS("/inner/revision/myRevisions", true),
    PROJECT("/inner/project/project", true),
    PROJECTS("/inner/project/projects", true),
    ADD_PROJECT("/inner/project/addProject", true),
    EDIT_PROJECT("/inner/project/editProject", true),
    MY_SETTINGS("/inner/user/mySettings", true),
    EDIT_USER("/inner/user/editUser", true),
    USER_INFO("/inner/user/userInfo", true),
    USERS_LIST("/inner/user/users", true),
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
