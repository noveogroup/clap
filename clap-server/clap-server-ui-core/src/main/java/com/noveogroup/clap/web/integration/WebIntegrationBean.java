package com.noveogroup.clap.web.integration;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andrey Sokolov
 */
@Named
public class WebIntegrationBean {

    @Inject
    private AuthenticationIntegrationView authenticationIntegrationView;

    public AuthenticationIntegrationView getAuthenticationIntegrationView() {
        return authenticationIntegrationView;
    }
}
