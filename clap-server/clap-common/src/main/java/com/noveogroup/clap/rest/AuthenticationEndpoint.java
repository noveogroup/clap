package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.auth.ApkAuthentication;
import com.noveogroup.clap.model.auth.Authentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * @author Andrey Sokolov
 */
@Path("/auth")
public interface AuthenticationEndpoint {

    @POST
    @Path("/common")
    @Consumes(MediaType.APPLICATION_JSON)
    String getToken(Authentication authentication);

    @POST
    @Path("/apk")
    @Consumes(MediaType.APPLICATION_JSON)
    String getToken(ApkAuthentication authentication);
}
