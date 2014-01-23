package com.noveogroup.clap.web.filter;

import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.user.UserService;
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
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
public class LoadUserToSessionFilter implements Filter {

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private UserService userService;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        //no data in session
        if (!userSessionData.isAuthenticated()) {
            final Subject subject = SecurityUtils.getSubject();
            //but have authenticated subject
            if (subject.isAuthenticated()) {
                //time to load user in session bean
                final String login = (String) subject.getPrincipals().getPrimaryPrincipal();
                final User user = userService.getUser(login);
                userSessionData.setUser(user);
                userSessionData.setAuthenticated(true);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}
