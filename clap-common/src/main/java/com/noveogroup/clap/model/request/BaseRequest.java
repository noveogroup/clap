package com.noveogroup.clap.model.request;

import com.noveogroup.clap.model.auth.Authentication;

import javax.validation.constraints.NotNull;

/**
 * @author Andrey Sokolov
 */
public class BaseRequest {
    @NotNull
    private Authentication authentication;

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
