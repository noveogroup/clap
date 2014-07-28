package com.noveogroup.clap.plugin.tasks

import com.noveogroup.clap.dataprovider.model.ProjectInfoImpl
import com.noveogroup.clap.generator.RevisionGenerator
import com.noveogroup.clap.plugin.ClapPlugin
import com.sun.codemodel.JClassAlreadyExistsException
import org.gradle.api.tasks.TaskAction

/**
 * @author Andrey Sokolov
 */
class GenerateVersionTask extends AbstractClapTask{

    @TaskAction
    def generateVersion(){
        try {
            ProjectInfoImpl projectInfo = new ProjectInfoImpl();
            String revisionId = getRevisionId();
            String projectId = getProjectExternalId();
            projectInfo.setRevisionId(revisionId);
            projectInfo.setProjectId(projectId);
            RevisionGenerator revisionGenerator = new RevisionGenerator();

            String className = "RevisionImpl"
            String packageName = "com.noveogroup.clap"

            String generatedSourcePath = ClapPlugin.PLUGIN_CONTEXT.get(ClapPlugin.GENERATED_SOURCES_DIR_KEY)

            File generatedSourcesDirectory = new File(generatedSourcePath)



            revisionGenerator.generate(generatedSourcesDirectory, className, packageName, projectInfo);

            //project.addCompileSourceRoot(generatedSourcesDirectory.getAbsolutePath());



        } catch (JClassAlreadyExistsException e) {
            logger.error("Error while generating revision class " + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("Input/Output error while generating revision class " + e.getMessage(), e);
        }
    }
}
