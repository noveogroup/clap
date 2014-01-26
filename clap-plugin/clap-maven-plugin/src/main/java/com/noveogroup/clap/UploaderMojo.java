package com.noveogroup.clap;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.rest.AuthenticationEndpoint;
import com.noveogroup.clap.rest.RevisionEndpoint;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.jboss.resteasy.client.ProxyFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Mikhail Demidov
 */
@Mojo(name = "upload", defaultPhase = LifecyclePhase.DEPLOY, threadSafe = true)
public class UploaderMojo extends AbstractClapMojo {


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String revisionId = getRevisionId();
        getLog().info("generated revisionId: " + revisionId);
        String projectId = getProjectExternalId();
        getLog().info("generated projectId: " + projectId);

        StringBuilder artifactPathBuilder = new StringBuilder();
        artifactPathBuilder
                .append(project.getBasedir())
                .append("/target/");

        if (StringUtils.isEmpty(artifactName)) {
            artifactPathBuilder
                    .append(project.getBuild().getFinalName())
                    .append(".")
                    .append(project.getPackaging());
        } else {
            artifactPathBuilder.append(artifactName);
        }
        File artifactFile = new File(artifactPathBuilder.toString());
        if (!artifactFile.exists()) {
            getLog().error("Artifact not exists");
        } else {
            try {
                final FileInputStream data = new FileInputStream(artifactFile);
                final RevisionEndpoint revisionEndpoint = ProxyFactory.create(RevisionEndpoint.class, serviceUrl);
                final AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class,
                        serviceUrl);

                final Authentication user = new Authentication();
                user.setLogin(clapLogin);
                user.setPassword(clapPassword);
                final String token = authenticationEndpoint.getToken(user);
                getLog().debug("token - " + token);

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
                getLog().info("HASH " + revision.getHash());
            } catch (FileNotFoundException e) {
                getLog().error("Error while uploading artifact " + e.getMessage(), e);
            } catch (IOException e) {
                getLog().error("Error while uploading artifact " + e.getMessage(), e);
            }
        }

    }
}
