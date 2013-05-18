package com.noveogroup.clap.rest;

import com.noveogroup.clap.rest.model.SendMessageRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/message")
public interface MessagesEndpoint {

    @POST
    @Path("sendMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    void saveMessage(SendMessageRequest request);
}
