package com.noveogroup.clap.web.auth;

import com.noveogroup.clap.auth.AuthenticationRequestHelper;
import com.noveogroup.clap.integration.RequestHelperFactory;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Crooked nail to "inject" requestHelper to service ejb module
 *
 * @author Andrey Sokolov
 */
public class AddRequestHelperFilter implements Filter {

    @Inject
    private RequestHelperFactory requestHelperFactory;

    @Inject
    private AuthenticationRequestHelper authenticationRequestHelper;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        addRequestHelper();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    public void addRequestHelper() {
        requestHelperFactory.addRequestHelper(authenticationRequestHelper);
    }
}
