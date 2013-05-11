package com.noveogroup.clap.web.controller;

import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsModel;
import com.noveogroup.clap.web.model.RevisionsModel;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Date;

@Named
@RequestScoped
public class RevisionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionsController.class);
    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    @Inject
    private RevisionService revisionService;

    @Inject
    private ProjectService projectService;

    public String saveNewRevision() throws IOException {
        LOGGER.debug("saving new revision");
        ProjectDTO projectDTO = projectsModel.getSelectedProject();
        RevisionDTO revisionDTO = new RevisionDTO();
        revisionDTO.setRevisionType(RevisionType.DEVELOP);
        revisionDTO.setTimestamp(new Date().getTime());
        UploadedFile newRevisionCleanApk = revisionsModel.getNewRevisionCleanApk();
        UploadedFile newRevisionHackedApk = revisionsModel.getNewRevisionHackedApk();
        revisionService.addRevision(projectDTO.getId(),
                revisionDTO,
                newRevisionCleanApk != null ? newRevisionCleanApk.getContents() : null,
                newRevisionHackedApk != null ? newRevisionHackedApk.getContents() : null);
        revisionsModel.reset();
        LOGGER.debug("revision saved");
        projectsModel.setSelectedProject(projectService.findById(projectsModel.getSelectedProject().getId()));
        return Navigation.SAME_PAGE.getView();
    }
}
