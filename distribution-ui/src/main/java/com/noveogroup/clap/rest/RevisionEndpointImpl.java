package com.noveogroup.clap.rest;


import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.service.revision.RevisionService;
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
    private RevisionService revisionService;


    @Override
    public RevisionDTO createRevision(final String projectId
            , final InputStream mainPackageInputStream
            , final FormDataContentDisposition mainPackageDetail
            , final InputStream specialPackageInputStream
            , final FormDataContentDisposition specialPackageDetail) {

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
        RevisionDTO revisionDTO = new RevisionDTO();
        return revisionService.addRevision(id, revisionDTO, mainPackage, specialPackage);
    }
}