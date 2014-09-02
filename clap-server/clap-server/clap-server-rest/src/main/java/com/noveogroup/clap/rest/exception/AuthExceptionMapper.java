package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.model.response.ClapResponse;
import org.apache.shiro.authc.AuthenticationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Andrey Sokolov
 */
public class AuthExceptionMapper extends BaseAuthExceptionMapper implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(final AuthenticationException e) {
        final ClapResponse clapResponse = new ClapResponse();
        final Throwable cause = e.getCause();
        if (cause instanceof com.noveogroup.clap.exception.ClapException) {
            fillResponse(clapResponse, (com.noveogroup.clap.exception.ClapException) cause);
        } else {
            clapResponse.setCode(ClapResponse.ERROR_CODE_AUTH_FAILED);
            clapResponse.setMessage(e.getMessage());
        }
        return Response.status(Response.Status.FORBIDDEN)
                .entity(clapResponse)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
