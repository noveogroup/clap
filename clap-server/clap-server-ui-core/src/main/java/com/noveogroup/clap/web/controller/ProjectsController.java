package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.exception.ClapPersistenceException;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsListDataModel;
import com.noveogroup.clap.web.model.ProjectsModel;
import com.noveogroup.clap.web.model.RevisionsListDataModel;
import com.noveogroup.clap.web.model.RevisionsModel;
import com.noveogroup.clap.web.model.StreamedImagedProject;
import com.noveogroup.clap.web.util.message.MessageSupport;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
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
    private MessageSupport messageSupport;

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    public String addProject() {
        LOGGER.debug("add project : " + projectsModel.getNewProject());
        try {
            projectService.createProject(projectsModel.getNewProject());
        } catch (ClapPersistenceException e){
            throw new ClapPersistenceException(messageSupport.getMessage("project.add.message.exists"),e.getCause());
        }
        projectsModel.setNewProject(new Project());
        LOGGER.debug("project saved");
        return Navigation.PROJECTS.getView();
    }

    public void prepareProjectsListView() {
        LOGGER.debug("call project service");
        final List<ImagedProject> projectList = projectService.findAllImagedProjects();
        LOGGER.debug("project service ret " + projectList);
        if (projectList != null) {
            final List<StreamedImagedProject> streamedImagedProjects = new ArrayList<StreamedImagedProject>();
            for (final ImagedProject project : projectList) {
                streamedImagedProjects.add(new StreamedImagedProject(project));
            }
            projectsModel.setProjectsListDataModel(new ProjectsListDataModel(streamedImagedProjects));
            LOGGER.debug(projectList.size() + " projects loaded");
        }
    }

    public void prepareProjectView() {
        final Project selectedProject = projectsModel.getSelectedProject();
        if (selectedProject != null) {
            final ImagedProject projectWithRevisions = projectService.findByIdWithImage(selectedProject.getId());
            if (projectWithRevisions != null) {
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
