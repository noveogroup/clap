package com.noveogroup.clap.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
public class ResourceDebugFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceDebugFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOGGER.debug("init");
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Throwable e) {
            LOGGER.error("error while processing resources", e);
        }
    }

    @Override
    public void destroy() {
        LOGGER.debug("destroy");
    }
}
