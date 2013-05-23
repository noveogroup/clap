package com.noveogroup.clap.interceptor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * @author Andrey Sokolov
 */
@RequestScoped
public class ContextProvider {

    private FacesContext servletContext;

    @PostConstruct
    protected void init(){
        servletContext = FacesContext
                .getCurrentInstance();
    }

    public FacesContext getServletContext() {
        return servletContext;
    }
}
