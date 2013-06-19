package com.noveogroup.clap.integration.web;

import com.noveogroup.clap.web.integration.AuthenticationIntegrationView;

/**
 * @author Andrey Sokolov
 */
public class AuthenticationIntegrationViewImpl implements AuthenticationIntegrationView {
    @Override
    public String viewName() {
        return "customIntegrationView123?faces-redirect=true";
    }
}
