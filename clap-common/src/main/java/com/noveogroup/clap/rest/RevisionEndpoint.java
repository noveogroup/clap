package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.revision.Revision;
import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Mikhail Demidov
 */
@Path("/revision")
public interface RevisionEndpoint {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @BadgerFish
    Revision createOrUpdateRevision(@MultipartForm CreateOrUpdateRevisionRequest request);




//    @POST
//    @Path("createRevision")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    Revision createOrUpdateRevision(@FormDataParam("authenticationKey") String authenticationKey,
//                            @FormDataParam("projectExternalId") String projectExternalId,
//                            @FormDataParam("revisionHash") String revisionHash,
//                            @FormDataParam("mainPackage") InputStream mainPackageInputStream,
//                            @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail,
//                            @FormDataParam("specialPackage") InputStream specialPackageInputStream,
//                            @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);
//
//    @POST
//    @Path("updateRevisionPackages")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    Revision updateRevisionPackages(@FormDataParam("authenticationKey") String authenticationKey,
//                                    @FormDataParam("revisionHash") String revisionHash,
//                                    @FormDataParam("mainPackage") InputStream mainPackageInputStream,
//                                    @FormDataParam("mainPackage") FormDataContentDisposition mainPackageDetail,
//                                    @FormDataParam("specialPackage") InputStream specialPackageInputStream,
//                                    @FormDataParam("specialPackage") FormDataContentDisposition specialPackageDetail);


    @POST
    @Path("getRevision")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Revision getRevision(RevisionRequest request);

}
