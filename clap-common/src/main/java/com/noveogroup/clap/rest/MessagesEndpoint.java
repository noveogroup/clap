package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/message")
public interface MessagesEndpoint {

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    void saveCrashMessage(SendMessageRequest request);


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    void saveScreenshot(@MultipartForm ScreenshotMessageRequest request);
}
