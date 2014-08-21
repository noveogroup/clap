package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.exception.ClapAuthenticationException;
import com.noveogroup.clap.model.response.ClapResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Andrey Sokolov
 */
public class AuthExceptionMapper implements ExceptionMapper<ClapAuthenticationException> {
    @Override
    public Response toResponse(final ClapAuthenticationException e) {
        final ClapResponse clapResponse = new ClapResponse();
        clapResponse.setCode(ClapResponse.ERROR_CODE_AUTH_FAILED);
        clapResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.FORBIDDEN)
                .entity(clapResponse)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
