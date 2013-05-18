package com.noveogroup.clap.rest;


import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.revision.RevisionService;
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
        return revisionService.addRevision(id, revision, mainPackage, specialPackage);
    }

    @Override
    public Revision updateRevisionPackages(Long revisionTimestamp,
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
        return revisionService.updateRevisionPackages(revisionTimestamp,mainPackage,specialPackage);
    }

    @Override
    public Response downloadAPK(final long id, final int type) {
        ApplicationFile application = revisionService.getApplication(id, type);
        return Response.ok(application.getContent()).header("Content-Disposition", "attachment; filename=\""+application.getFilename()+"\"").build();
    }

    @Override
    public Revision getRevisionByTimestamp(long timestamp) {
        return revisionService.getRevision(timestamp);
    }
}