package com.noveogroup.clap.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@ApplicationScoped
@Path("/")
public class TestController {

    @GET
    @Path("echo")
    public String echo(@QueryParam("q") String original) {
        return original;
    }
}
