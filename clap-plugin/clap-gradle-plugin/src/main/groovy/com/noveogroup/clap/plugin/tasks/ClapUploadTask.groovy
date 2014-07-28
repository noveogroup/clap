package com.noveogroup.clap.plugin.tasks

import com.noveogroup.clap.model.auth.Authentication
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest
import com.noveogroup.clap.model.revision.Revision
import com.noveogroup.clap.plugin.ClapPlugin
import com.noveogroup.clap.plugin.config.Clap
import com.noveogroup.clap.rest.AuthenticationEndpoint
import com.noveogroup.clap.rest.RevisionEndpoint
import com.noveogroup.clap.utils.Constants
import com.noveogroup.clap.utils.HashCalculator
import org.apache.commons.lang.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jboss.resteasy.client.ProxyFactory

/**
 * @author Andrey Sokolov
 */
class ClapUploadTask extends DefaultTask {

    def checksumAlgorithm = "MD5"

    @TaskAction
    def uploadArchives(){
        //TODO check how to get clean apk in gradle
        boolean isMainPackage = false;

        String revisionId = getRevisionId();
        logger.info("generated revisionId: " + revisionId);
        String projectId = getProjectExternalId();
        logger.info("generated projectId: " + projectId);


        //TODO get artifact file(s)
        StringBuilder artifactPathBuilder = new StringBuilder();
        /*artifactPathBuilder
                .append(project.getBasedir())
                .append("/target/");

        if (StringUtils.isEmpty(artifactName)) {
            artifactPathBuilder
                    .append(project.getBuild().getFinalName())
                    .append(".")
                    .append(project.getPackaging());
        } else {
            artifactPathBuilder.append(artifactName);
        } */
        File artifactFile = new File(artifactPathBuilder.toString());
        if (!artifactFile.exists()) {
            logger.error("Artifact not exists");
        } else {
            try {
                def clap = project.extensions.getByType(Clap)

                final FileInputStream data = new FileInputStream(artifactFile);
                final RevisionEndpoint revisionEndpoint = ProxyFactory.create(RevisionEndpoint.class, clap.serviceUrl);
                final AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class,
                        clap.serviceUrl);

                final Authentication user = new Authentication();
                user.setLogin(clap.clapLogin);
                user.setPassword(clap.clapPassword);
                final String token = authenticationEndpoint.getToken(user);
                logger.debug("token - " + token);

                CreateOrUpdateRevisionRequest createOrUpdateRevisionRequest = new CreateOrUpdateRevisionRequest();
                createOrUpdateRevisionRequest.setToken(token);
                if (isMainPackage) {
                    createOrUpdateRevisionRequest.setMainPackage(data);
                } else {
                    createOrUpdateRevisionRequest.setSpecialPackage(data);
                }
                createOrUpdateRevisionRequest.setRevisionHash(revisionId);
                createOrUpdateRevisionRequest.setProjectExternalId(projectId);
                Revision revision = revisionEndpoint.createOrUpdateRevision(createOrUpdateRevisionRequest);
                logger.info("HASH " + revision.getHash());
            } catch (FileNotFoundException e) {
                logger.error("Error while uploading artifact " + e.getMessage(), e);
            } catch (IOException e) {
                logger.error("Error while uploading artifact " + e.getMessage(), e);
            }
        }
    }


    public String getProjectExternalId() {
        String projectId = (String) ClapPlugin.PLUGIN_CONTEXT.get(Constants.PROJECT_ID);
        if (StringUtils.isEmpty(projectId)) {
            def clap = project.extensions.getByType(Clap)
            def projectExternalId = clap.clapProjectId
            ClapPlugin.PLUGIN_CONTEXT.put(Constants.PROJECT_ID,projectExternalId)
            return projectExternalId;
        } else {
            return projectId;
        }
    }


    protected String getRevisionId() {
        String revisionId = (String) ClapPlugin.PLUGIN_CONTEXT.get(Constants.REVISION_ID);
        if (StringUtils.isEmpty(revisionId)) {
            def clap = project.extensions.getByType(Clap)
            def config = clap.hashCalculatorConfig
            HashCalculator hashCalculator = new HashCalculator(config.baseDir, config.folders, config.excludes);
            def calculateHash = hashCalculator.calculateHash(checksumAlgorithm)
            ClapPlugin.PLUGIN_CONTEXT.put(Constants.REVISION_ID,calculateHash);
            return calculateHash;
        } else {
            return revisionId;
        }
    }
}
