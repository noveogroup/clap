package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.response.ClapResponse;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dedicated endpoint to upload files
 * resteasy proxy won't work on android for endpoints that contains multipart-form methods
 *
 * @author Andrey Sokolov
 */
@Path("/upload")
public interface UploadFileEndpoint {

    @POST
    @Path("/revision")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    ClapResponse createOrUpdateRevision(@MultipartForm CreateOrUpdateRevisionRequest request);

    @POST
    @Path("/screenshot")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    ClapResponse saveScreenshot(@MultipartForm ScreenshotMessageRequest request);
}
