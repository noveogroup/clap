package com.noveogroup.clap.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@Path("/misc")
public interface MiscEndpoint {

    @GET
    @Path("/qrCode/")
    Response getQRCode(@QueryParam("url") String url) throws IOException;
}
