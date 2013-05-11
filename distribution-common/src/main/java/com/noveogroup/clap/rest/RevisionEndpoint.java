package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.revision.RevisionDTO;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * @author Mikhail Demidov
 */
@Path("/revision")
public interface RevisionEndpoint {


    @POST
    @Path("createRevision")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    RevisionDTO createRevision(@FormDataParam("projectId") String projectId
            , @FormDataParam("mainPackage") InputStream mainPackageInputStream
            , @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail
            , @FormDataParam("specialPackage") InputStream specialPackageInputStream
            , @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);

}
