package com.noveogroup.clap.dataprovider.model;

import com.noveogroup.clap.ProjectInfo;

/**
 * @author Mikhail Demidov
 */
public class ProjectInfoImpl implements ProjectInfo {

    private String revisionId;
    private String projectId;


    @Override
    public String getRevisionId() {
        return revisionId;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    public void setRevisionId(final String revisionId) {
        this.revisionId = revisionId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }
}
