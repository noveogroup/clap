package com.noveogroup.clap.rest;


import com.noveogroup.clap.model.request.revision.GetApplicationRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.rest.RevisionEndpoint;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.web.controller.ProjectsController;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private RevisionService revisionService;


    @Override
    public Revision createRevision(final String projectId,
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
            if (specialPackage != null) {
                specialPackage = IOUtils.toByteArray(specialPackageInputStream);
            }
        } catch (IOException e) {
            LOGGER.error("Error while uploading apk " + e.getMessage(), e);
        }
        Long id = Long.parseLong(projectId);
        Revision revision = new Revision();
        AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        request.setProjectExternalId(projectId);
        request.setRevision(revision);
        request.setMainPackage(mainPackage);
        request.setSpecialPackage(specialPackage);
        return revisionService.addOrGetRevision(request);
    }

    @Override
    public Revision updateRevisionPackages(Long revisionId,
                                              InputStream mainPackageInputStream,
                                              FormDataContentDisposition mainPackageDetail,
                                              InputStream specialPackageInputStream,
                                              FormDataContentDisposition specialPackageDetail) {
        byte[] mainPackage = null;
        byte[] specialPackage = null;
        try {
            if (mainPackageInputStream != null) {
                mainPackage = IOUtils.toByteArray(mainPackageInputStream);
            }
            if (specialPackage != null) {
                specialPackage = IOUtils.toByteArray(specialPackageInputStream);
            }
        } catch (IOException e) {
            LOGGER.error("Error while uploading apk " + e.getMessage(), e);
        }
        UpdateRevisionPackagesRequest request = new UpdateRevisionPackagesRequest();
        request.setRevisionId(revisionId);
        request.setMainPackage(mainPackage);
        request.setSpecialPackage(specialPackage);
        return revisionService.updateRevisionPackages(request);
    }

    @Override
    public Response downloadAPK(final long id, final int type) {
        //TODO fix authentication and app type
        GetApplicationRequest request = new GetApplicationRequest();
        request.setRevisionId(id);
        request.setApplicationType(type == 0 ? ApplicationType.MAIN : ApplicationType.SPECIAL);
        ApplicationFile application = revisionService.getApplication(request);
        return Response.ok(application.getContent()).header("Content-Disposition", "attachment; filename=\""+application.getFilename()+"\"").build();
    }

    @Override
    public Revision getRevision(RevisionRequest request) {
        return revisionService.getRevision(request);
    }
}