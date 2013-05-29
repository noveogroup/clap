package com.noveogroup.clap.rest;


import com.noveogroup.clap.facade.RevisionsFacade;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.web.controller.ProjectsController;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private RevisionsFacade revisionsFacade;


    @Override
    public Revision createOrUpdateRevision(final String authenticationKey,
                                   final String projectId,
                                   final String revisionHash,
                                   final InputStream mainPackageInputStream,
                                   final FormDataContentDisposition mainPackageDetail,
                                   final InputStream specialPackageInputStream,
                                   final FormDataContentDisposition specialPackageDetail) {

        byte[] mainPackage = null;
        byte[] specialPackage = null;
        try {
            if (mainPackageInputStream != null) {
                mainPackage = IOUtils.toByteArray(mainPackageInputStream);
            }
            if (specialPackageInputStream != null) {
                specialPackage = IOUtils.toByteArray(specialPackageInputStream);
            }
        } catch (IOException e) {
            LOGGER.error("Error while uploading apk " + e.getMessage(), e);
        }
        final Long id = Long.parseLong(projectId);
        final Revision revision = new Revision();
        revision.setHash(revisionHash);
        final AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        request.setProjectExternalId(projectId);
        request.setRevision(revision);
        request.setMainPackage(mainPackage);
        request.setSpecialPackage(specialPackage);
        return revisionsFacade.addOrGetRevision(request);
    }

    @Override
    public Revision updateRevisionPackages(final String authenticationKey,
                                           final String revisionHash,
                                           final InputStream mainPackageInputStream,
                                           final FormDataContentDisposition mainPackageDetail,
                                           final InputStream specialPackageInputStream,
                                           final FormDataContentDisposition specialPackageDetail) {
        byte[] mainPackage = null;
        byte[] specialPackage = null;
        try {
            if (mainPackageInputStream != null) {
                mainPackage = IOUtils.toByteArray(mainPackageInputStream);
            }
            if (specialPackageInputStream != null) {
                specialPackage = IOUtils.toByteArray(specialPackageInputStream);
            }
        } catch (IOException e) {
            LOGGER.error("Error while uploading apk " + e.getMessage(), e);
        }
        final UpdateRevisionPackagesRequest request = new UpdateRevisionPackagesRequest();
        request.setRevisionHash(revisionHash);
        request.setMainPackage(mainPackage);
        request.setSpecialPackage(specialPackage);
        return revisionsFacade.updateRevisionPackages(request);
    }

    @Override
    public Revision getRevision(final RevisionRequest request) {
        return revisionsFacade.getRevision(request);
    }
}