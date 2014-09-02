package com.noveogroup.clap.rest.exception;

import com.noveogroup.clap.exception.ClapTokenExpirationException;
import com.noveogroup.clap.model.response.ClapResponse;

/**
 * @author Andrey Sokolov
 */
public class BaseAuthExceptionMapper {


    protected void fillResponse(final ClapResponse clapResponse,
                                final com.noveogroup.clap.exception.ClapException cause) {
        if (cause instanceof ClapTokenExpirationException) {
            clapResponse.setCode(ClapResponse.ERROR_CODE_TOKEN_EXPIRED);
        } else {
            clapResponse.setCode(ClapResponse.ERROR_CODE_AUTH_FAILED);
        }
        clapResponse.setMessage(cause.getMessage());
    }
}
