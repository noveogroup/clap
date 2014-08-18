package com.noveogroup.clap.dataprovider.model;

import com.noveogroup.clap.ProjectInfo;

/**
 * @author Mikhail Demidov
 */
public class ProjectInfoImpl implements ProjectInfo {

    private String revisionId;
    private String projectId;
    private String buildByLogin;
    private String buildByPassword;
    private String clapHost;


    @Override
    public String getRevisionId() {
        return revisionId;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public String getBuildByLogin() {
        return buildByLogin;
    }

    public void setBuildByLogin(final String buildByLogin) {
        this.buildByLogin = buildByLogin;
    }

    @Override
    public String getBuildByPassword() {
        return buildByPassword;
    }

    public void setBuildByPassword(final String buildByPassword) {
        this.buildByPassword = buildByPassword;
    }

    public void setRevisionId(final String revisionId) {
        this.revisionId = revisionId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String getClapHost() {
        return clapHost;
    }

    public void setClapHost(final String clapHost) {
        this.clapHost = clapHost;
    }
}
