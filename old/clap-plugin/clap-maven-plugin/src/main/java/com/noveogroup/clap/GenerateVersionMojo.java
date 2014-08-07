package com.noveogroup.clap;

import com.noveogroup.clap.dataprovider.model.ProjectInfoImpl;
import com.noveogroup.clap.generator.RevisionGenerator;
import com.noveogroup.clap.utils.Constants;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;

/**
 * Generate version mojo
 */
@Mojo(name = "version", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GenerateVersionMojo extends AbstractClapMojo {


    public void execute()
            throws MojoExecutionException {
        try {
            ProjectInfoImpl projectInfo = new ProjectInfoImpl();
            String revisionId = getRevisionId();
            String projectId = getProjectExternalId();
            projectInfo.setRevisionId(revisionId);
            projectInfo.setProjectId(projectId);
            RevisionGenerator revisionGenerator = new RevisionGenerator();
            revisionGenerator.generate(generatedSourcesDirectory, className, packageName, projectInfo);
            project.addCompileSourceRoot(generatedSourcesDirectory.getAbsolutePath());

            getPluginContext().put(Constants.REVISION_ID, revisionId);
            getPluginContext().put(Constants.PROJECT_ID, projectId);

        } catch (JClassAlreadyExistsException e) {
            getLog().error("Error while generating revision class " + e.getMessage(), e);
        } catch (IOException e) {
            getLog().error("Input/Output error while generating revision class " + e.getMessage(), e);
        }
    }
}
