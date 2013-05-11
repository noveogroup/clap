package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.revision.RevisionDTO;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    RevisionDTO createRevision(@FormDataParam("projectId") String projectId,
                               @FormDataParam("mainPackage") InputStream mainPackageInputStream,
                               @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail,
                               @FormDataParam("specialPackage") InputStream specialPackageInputStream,
                               @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);

    @POST
    @Path("updateRevisionPackages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    RevisionDTO updateRevisionPackages(@FormDataParam("revisionTimestamp") Long revisionTimestamp,
                                       @FormDataParam("mainPackage") InputStream mainPackageInputStream,
                                       @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail,
                                       @FormDataParam("specialPackage") InputStream specialPackageInputStream,
                                       @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);

    @GET
    @Path("downloadAPK/{id}/{type}")
    @Produces("application/vnd.android.package-archive")
    Response downloadAPK(@PathParam("id") long id, @PathParam("type") int type);

}
