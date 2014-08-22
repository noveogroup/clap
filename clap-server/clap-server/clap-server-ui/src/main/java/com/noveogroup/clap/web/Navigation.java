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
        return getView(true);
    }

    public String getView(boolean includeViewParams) {
        final StringBuilder builder = new StringBuilder().append(view);
        if (facesRedirect) {
            builder.append("?faces-redirect=true");
        }
        if(includeViewParams){
            if(!facesRedirect){
                builder.append("?");
            } else {
                builder.append("&");
            }
            builder.append("includeViewParams=true");
        }
        return builder.toString();
    }

}
