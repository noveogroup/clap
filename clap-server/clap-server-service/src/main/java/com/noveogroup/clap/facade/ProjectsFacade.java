package com.noveogroup.clap.facade;

import com.noveogroup.clap.auth.AuthenticationRequired;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.transaction.Transactional;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({ClapMainInterceptor.class})
public class ProjectsFacade {

    @Inject
    private ProjectService projectService;

    @AuthenticationRequired
    @Transactional
    public Project createProject(final Project project){
        return projectService.createProject(project);
    }

    @AuthenticationRequired
    @Transactional
    public Project getCreateUpdateProject(final Project project){
        return projectService.getCreateUpdateProject(project);
    }

    @AuthenticationRequired
    @Transactional
    public Project save(final Project project){
        return projectService.save(project);
    }

    @AuthenticationRequired
    @Transactional
    public Project findById(final Long id){
        return projectService.findById(id);
    }

    @AuthenticationRequired
    @Transactional
    public ImagedProject findByIdWithImage(final Long id){
        return projectService.findByIdWithImage(id);
    }

    @AuthenticationRequired
    public List<Project> findAllProjects(){
        return projectService.findAllProjects();
    }

    @AuthenticationRequired
    public List<ImagedProject> findAllImagedProjects(){
       return projectService.findAllImagedProjects();
    }
}
