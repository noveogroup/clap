package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.request.message.CrashMessageRequest;
import com.noveogroup.clap.model.request.message.InfoMessageRequest;
import com.noveogroup.clap.model.request.message.LogsBunchMessageRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/message")
public interface MessagesEndpoint {

    @Path("/crash")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void saveCrashMessage(CrashMessageRequest request);

    @Path("/logs")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void saveLogsBunchMessage(LogsBunchMessageRequest request);


    @GET
    @Path("/screenshot/{messageId}")
    Response getScreenshot(@PathParam("messageId") long messageId);

    @Path("/info")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void saveInfoMessage(InfoMessageRequest request);
}
