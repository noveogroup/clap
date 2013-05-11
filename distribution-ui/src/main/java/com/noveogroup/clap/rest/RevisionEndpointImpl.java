package com.noveogroup.clap.rest;


import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.web.controller.ProjectsController;
import com.noveogroup.clap.web.model.RevisionsModel;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import java.io.InputStream;

/**
 * @author Mikhail Demidov
 */
@ApplicationScoped
public class RevisionEndpointImpl implements RevisionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    @Inject
    private RevisionsModel revisionsModel;

    public String saveNewRevision() {
        //TODO
        return null;
    }

    @Override
    public RevisionDTO createRevision(@QueryParam("projectId") final Long projectId, @FormDataParam("mainPackage") final InputStream mainPackageInputStream, @FormDataParam("mainPackage") final FormDataContentDisposition mainPackageDetail) {

        return null;
    }
}