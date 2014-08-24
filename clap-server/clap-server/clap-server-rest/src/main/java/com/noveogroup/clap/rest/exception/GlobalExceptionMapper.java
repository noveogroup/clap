package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.model.response.ClapResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Andrey Sokolov
 */
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(final Exception e) {
        final ClapResponse clapResponse = new ClapResponse();
        clapResponse.setCode(ClapResponse.ERROR_CODE_UNIDENTIFIED_ERROR);
        clapResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(clapResponse)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
