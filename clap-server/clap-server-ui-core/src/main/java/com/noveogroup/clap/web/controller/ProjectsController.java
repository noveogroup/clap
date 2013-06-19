package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsListDataModel;
import com.noveogroup.clap.web.model.ProjectsModel;
import com.noveogroup.clap.web.model.RevisionsListDataModel;
import com.noveogroup.clap.web.model.RevisionsModel;
import com.noveogroup.clap.web.model.StreamedImagedProject;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class ProjectsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsController.class);

    private long projectId;

    @Inject
    private ProjectService projectsFacade;

    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    public String addProject() {
        try {
            LOGGER.debug("add project : " + projectsModel.getNewProject());
            projectsFacade.createProject(projectsModel.getNewProject());
            projectsModel.setNewProject(new Project());
            LOGGER.debug("project saved");
            return Navigation.PROJECTS.getView();
        } catch (Exception e){
            final FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Error while creating project");
            FacesContext.getCurrentInstance().addMessage(null,message);
            return Navigation.SAME_PAGE.getView();
        }
    }

    public void prepareProjectsListView() {
        final List<ImagedProject> projectList = projectsFacade.findAllImagedProjects();
        if(projectList != null){
            final List<StreamedImagedProject> streamedImagedProjects = new ArrayList<StreamedImagedProject>();
            for(final ImagedProject project : projectList){
                streamedImagedProjects.add(new StreamedImagedProject(project));
            }
            projectsModel.setProjectsListDataModel(new ProjectsListDataModel(streamedImagedProjects));
            LOGGER.debug(projectList.size() + " projects loaded");
        }
    }

    public void prepareProjectView(){
        final Project selectedProject = projectsModel.getSelectedProject();
        if(selectedProject != null){
            final ImagedProject projectWithRevisions = projectsFacade.findByIdWithImage(selectedProject.getId());
            if (projectWithRevisions != null){
                projectsModel.setSelectedProject(new StreamedImagedProject(projectWithRevisions));
                revisionsModel.setRevisionsListDataModel(
                        new RevisionsListDataModel(projectWithRevisions.getRevisions()));
            }
        } else {
            LOGGER.error("project not selected");
        }
    }


    public void onProjectSelect(final SelectEvent event) {
        final StreamedImagedProject selectedProject = (StreamedImagedProject) event.getObject();
        projectsModel.setSelectedProject(selectedProject);
        LOGGER.debug(selectedProject.getName() + " prject selected");
        redirectTo(Navigation.PROJECT);
    }


}
