package com.noveogroup.clap.library.api.rest;

import com.noveogroup.clap.library.api.model.auth.Authentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/auth")
public interface AuthenticationEndpoint {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    String getToken(Authentication authentication);
}
