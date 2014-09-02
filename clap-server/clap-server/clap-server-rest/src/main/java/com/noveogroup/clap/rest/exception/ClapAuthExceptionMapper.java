package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.exception.ClapAuthenticationException;
import com.noveogroup.clap.model.response.ClapResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Andrey Sokolov
 */
public class ClapAuthExceptionMapper extends BaseAuthExceptionMapper
        implements ExceptionMapper<ClapAuthenticationException> {
    @Override
    public Response toResponse(final ClapAuthenticationException e) {
        final ClapResponse clapResponse = new ClapResponse();
        fillResponse(clapResponse, e);
        return Response.status(Response.Status.FORBIDDEN)
                .entity(clapResponse)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}