package com.noveogroup.clap.rest;


import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.controller.ProjectsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private RevisionService revisionService;


//    @Override
//    public Revision createOrUpdateRevision(final String authenticationKey,
//                                   final String projectId,
//                                   final String revisionHash,
//                                   final InputStream mainPackageInputStream,
//                                   final FormDataContentDisposition mainPackageDetail,
//                                   final InputStream specialPackageInputStream,
//                                   final FormDataContentDisposition specialPackageDetail) {
//
//        byte[] mainPackage = null;
//        byte[] specialPackage = null;
//        try {
//            if (mainPackageInputStream != null) {
//                mainPackage = IOUtils.toByteArray(mainPackageInputStream);
//            }
//            if (specialPackageInputStream != null) {
//                specialPackage = IOUtils.toByteArray(specialPackageInputStream);
//            }
//        } catch (IOException e) {
//            LOGGER.error("Error while uploading apk " + e.getMessage(), e);
//        }
//        final Long id = Long.parseLong(projectId);
//        final Revision revision = new Revision();
//        revision.setHash(revisionHash);
//        final AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
//        request.setProjectExternalId(projectId);
//        request.setRevision(revision);
//        request.setMainPackage(mainPackage);
//        request.setSpecialPackage(specialPackage);
//        return revisionService.addOrGetRevision(request);
//    }
//
//    @Override
//    public Revision updateRevisionPackages(final String authenticationKey,
//                                           final String revisionHash,
//                                           final InputStream mainPackageInputStream,
//                                           final FormDataContentDisposition mainPackageDetail,
//                                           final InputStream specialPackageInputStream,
//                                           final FormDataContentDisposition specialPackageDetail) {
//        byte[] mainPackage = null;
//        byte[] specialPackage = null;
//        try {
//            if (mainPackageInputStream != null) {
//                mainPackage = IOUtils.toByteArray(mainPackageInputStream);
//            }
//            if (specialPackageInputStream != null) {
//                specialPackage = IOUtils.toByteArray(specialPackageInputStream);
//            }
//        } catch (IOException e) {
//            LOGGER.error("Error while uploading apk " + e.getMessage(), e);
//        }
//        final UpdateRevisionPackagesRequest request = new UpdateRevisionPackagesRequest();
//        request.setRevisionHash(revisionHash);
//        request.setMainPackage(mainPackage);
//        request.setSpecialPackage(specialPackage);
//        return revisionService.updateRevisionPackages(request);
//    }

    @Override
    public Revision createOrUpdateRevision(final CreateOrUpdateRevisionRequest request) {
        final Revision revision = new Revision();
        revision.setHash(request.getRevisionHash());
        final AddOrGetRevisionRequest addOrGetRevisionRequest = new AddOrGetRevisionRequest();
        addOrGetRevisionRequest.setProjectExternalId(request.getProjectExternalId());
        addOrGetRevisionRequest.setRevision(revision);
        if (request.getMainPackage() != null) {
            addOrGetRevisionRequest.setMainPackage(request.getMainPackage());
        }
        if (request.getSpecialPackage() != null) {
            addOrGetRevisionRequest.setSpecialPackage(request.getSpecialPackage());
        }
        if (revisionService == null) {
            throw new RuntimeException("Empty revisionService");
        }
        return revisionService.addOrGetRevision(addOrGetRevisionRequest);

    }

    @Override
    public Revision getRevision(final RevisionRequest request) {
        return revisionService.getRevision(request);
    }
}