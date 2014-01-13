package com.noveogroup.clap;

import com.noveogroup.clap.utils.Constants;
import com.noveogroup.clap.utils.HashCalculator;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * @author Mikhail Demidov
 */
public abstract class AbstractClapMojo extends AbstractMojo {

    @Parameter
    protected String[] folders;

    @Parameter
    protected String[] excludes;


    @Parameter(defaultValue = "MD5", property = "checksumAlgorithm", required = true)
    protected String checksumAlgorithm;

    @Parameter(defaultValue = "${basedir}")
    protected File baseDir;

    @Parameter(defaultValue = "${project.name}")
    protected String projectName;

    @Parameter(defaultValue = "${project.description}")
    protected String projectDescription;

    @Parameter(defaultValue = "RevisionImpl", property = "className", required = true)
    protected String className;

    @Parameter(defaultValue = "com.noveogroup.clap", property = "packageName", required = true)
    protected String packageName;


    @Parameter(defaultValue = "${project.build.directory}/generated-sources")
    protected File generatedSourcesDirectory;

    @Parameter(required = true)
    protected String serviceUrl;

    @Parameter(defaultValue = "${clap.login}")
    protected String clapLogin;

    @Parameter(defaultValue = "${clap.password}")
    protected String clapPassword;

    @Parameter
    protected boolean isMainPackage;

    @Parameter
    protected String artifactName;


    @Component
    protected MavenProject project;

    public String getProjectExternalId() {
        String projectId = (String) getPluginContext().get(Constants.PROJECT_ID);
        if (StringUtils.isEmpty(projectId)) {
            StringBuilder sb = new StringBuilder();
            sb.append(project.getGroupId())
                    .append(":")
                    .append(project.getArtifactId());
            return sb.toString();
        } else {
            return projectId;
        }
    }


    protected String getRevisionId() {
        String revisionId = (String) getPluginContext().get(Constants.REVISION_ID);
        if (StringUtils.isEmpty(revisionId)) {
            HashCalculator hashCalculator = new HashCalculator(baseDir, folders, excludes);
            return hashCalculator.calculateHash(checksumAlgorithm);
        } else {
            return revisionId;
        }
    }

}
