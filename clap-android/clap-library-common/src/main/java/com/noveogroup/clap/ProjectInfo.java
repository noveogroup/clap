package com.noveogroup.clap;

import com.noveogroup.clap.annotation.Parameter;

/**
 * @author Mikhail Demidov
 */
public interface ProjectInfo {

    @Parameter(parameterName = "revisionId")
    String getRevisionId();

    @Parameter(parameterName = "projectId")
    String getProjectId();
}
