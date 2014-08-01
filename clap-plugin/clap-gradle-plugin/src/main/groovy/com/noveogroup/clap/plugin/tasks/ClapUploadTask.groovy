package com.noveogroup.clap.plugin.tasks

import com.noveogroup.clap.model.auth.Authentication
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest
import com.noveogroup.clap.model.revision.Revision
import com.noveogroup.clap.plugin.config.Clap
import com.noveogroup.clap.rest.AuthenticationEndpoint
import com.noveogroup.clap.rest.RevisionEndpoint
import com.noveogroup.clap.rest.UploadFileEndpoint
import org.gradle.api.tasks.TaskAction
import org.jboss.resteasy.client.ProxyFactory

/**
 * @author Andrey Sokolov
 */
class ClapUploadTask extends AbstractClapTask {

    File artifactFile
    String variantName

    @TaskAction
    def uploadArchives(){
        //TODO check how to get clean apk in gradle
        boolean isMainPackage = false;

        String revisionId = getRevisionId();
        logger.info("generated revisionId: " + revisionId);
        String projectId = getProjectExternalId();
        logger.info("generated projectId: " + projectId);


        if (artifactFile == null || !artifactFile.exists()) {
            logger.error("Artifact not exists");
        } else {
            try {
                def clap = project.extensions.getByType(Clap)

                final FileInputStream data = new FileInputStream(artifactFile);
                final UploadFileEndpoint uploadFileEndpoint = ProxyFactory.create(UploadFileEndpoint.class, clap.serviceUrl);
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
                boolean created = uploadFileEndpoint.createOrUpdateRevision(createOrUpdateRevisionRequest);
                logger.info("Revision created:  " + created);
            } catch (FileNotFoundException e) {
                logger.error("Error while uploading artifact " + e.getMessage(), e);
            } catch (IOException e) {
                logger.error("Error while uploading artifact " + e.getMessage(), e);
            }
        }
    }
}
