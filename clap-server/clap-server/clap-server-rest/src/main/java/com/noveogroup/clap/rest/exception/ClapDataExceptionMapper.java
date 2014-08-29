package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.exception.ClapDataIntegrityException;
import com.noveogroup.clap.model.response.ClapResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Andrey Sokolov
 */
public class ClapDataExceptionMapper implements ExceptionMapper<ClapDataIntegrityException> {
    @Override
    public Response toResponse(final ClapDataIntegrityException e) {
        final ClapResponse clapResponse = new ClapResponse();
        clapResponse.setCode(ClapResponse.ERROR_CODE_DATA_ERROR);
        clapResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(clapResponse)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
