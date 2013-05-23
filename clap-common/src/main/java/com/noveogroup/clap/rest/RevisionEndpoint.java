package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
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
    Revision createOrUpdateRevision(@FormDataParam("authenticationKey") String authenticationKey,
                            @FormDataParam("projectExternalId") String projectExternalId,
                            @FormDataParam("revisionHash") String revisionHash,
                            @FormDataParam("mainPackage") InputStream mainPackageInputStream,
                            @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail,
                            @FormDataParam("specialPackage") InputStream specialPackageInputStream,
                            @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);

    @POST
    @Path("updateRevisionPackages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Revision updateRevisionPackages(@FormDataParam("authenticationKey") String authenticationKey,
                                    @FormDataParam("revisionHash") String revisionHash,
                                    @FormDataParam("mainPackage") InputStream mainPackageInputStream,
                                    @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail,
                                    @FormDataParam("specialPackage") InputStream specialPackageInputStream,
                                    @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);


    @POST
    @Path("getRevision")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Revision getRevision(RevisionRequest request);

}
