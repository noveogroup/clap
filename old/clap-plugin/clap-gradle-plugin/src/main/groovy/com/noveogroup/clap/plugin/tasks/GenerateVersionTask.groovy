package com.noveogroup.clap.plugin.tasks

import com.noveogroup.clap.dataprovider.model.ProjectInfoImpl
import com.noveogroup.clap.generator.RevisionGenerator
import com.noveogroup.clap.plugin.ClapPlugin
import com.sun.codemodel.JClassAlreadyExistsException
import org.gradle.api.tasks.TaskAction

/**
 */
class GenerateVersionTask extends AbstractClapTask{
    static final String CLAP_HOST = "http://10.0.14.53:8080/clap-rest";

    @TaskAction
    def generateVersion(){
        try {
            ProjectInfoImpl projectInfo = new ProjectInfoImpl();
            String revisionId = getRevisionId();
            String projectId = getProjectExternalId();
            projectInfo.setRevisionId(revisionId);
            projectInfo.setProjectId(projectId);
            def host = project.properties.get("clap.host")
            def login = project.properties.get("clap.login")
            def password = project.properties.get("clap.password")
            if(login == null){
                login = "unnamed"
                password = "unnamed_password"
            }
            if(host == null){
                host = CLAP_HOST
            }
            projectInfo.setClapHost(host)
            projectInfo.setBuildByLogin(login)
            projectInfo.setBuildByPassword(password)
            RevisionGenerator revisionGenerator = new RevisionGenerator();

            String className = "RevisionImpl"
            String packageName = "com.noveogroup.clap"

            String generatedSourcePath = ClapPlugin.PLUGIN_CONTEXT.get(ClapPlugin.GENERATED_SOURCES_DIR_KEY)

            File generatedSourcesDirectory = new File(generatedSourcePath)

            revisionGenerator.generate(generatedSourcesDirectory, className, packageName, projectInfo);

        } catch (JClassAlreadyExistsException e) {
            logger.error("Error while generating revision class " + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("Input/Output error while generating revision class " + e.getMessage(), e);
        }
    }
}
