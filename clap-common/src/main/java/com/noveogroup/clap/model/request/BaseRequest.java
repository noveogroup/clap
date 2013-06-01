package com.noveogroup.clap.model.request;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.auth.AuthenticationRequest;

import javax.validation.constraints.NotNull;

/**
 * @author Andrey Sokolov
 */
public class BaseRequest implements AuthenticationRequest {
    @NotNull
    private Authentication authentication;

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseRequest{");
        sb.append("authentication=").append(authentication);
        sb.append('}');
        return sb.toString();
    }
}
