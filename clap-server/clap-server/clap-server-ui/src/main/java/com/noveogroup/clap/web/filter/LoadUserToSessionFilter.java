package com.noveogroup.clap.web.filter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
import com.noveogroup.clap.web.config.WebConfigBean;
import com.noveogroup.clap.web.model.user.UserSessionData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class LoadUserToSessionFilter implements Filter {

    private static final List<String> processUrlPrefixes = Lists.newArrayList("/index", "/inner");

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private UserService userService;

    @Inject
    private WebConfigBean webConfigBean;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String servletPath = httpServletRequest.getServletPath();
        boolean process = false;
        for (String processingPath : processUrlPrefixes) {
            if (servletPath.startsWith(processingPath)) {
                process = true;
                break;
            }
        }
        if (process) {
            final Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                final User user = userService.getUser(webConfigBean.isAutoCreateUsers());
                userSessionData.setUser(user);
                userSessionData.setAuthenticated(true);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
