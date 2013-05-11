package com.noveogroup.clap.web.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.ProjectsModel;
import com.noveogroup.clap.web.model.RevisionsListDataModel;
import com.noveogroup.clap.web.model.RevisionsModel;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Named
@RequestScoped
public class RevisionsController extends BaseController{

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
        UploadedFile newRevisionCleanApk = revisionsModel.getUploadCleanApk();
        UploadedFile newRevisionHackedApk = revisionsModel.getUploadHackedApk();
        revisionService.addRevision(projectDTO.getId(),
                revisionDTO,
                newRevisionCleanApk != null ? newRevisionCleanApk.getContents() : null,
                newRevisionHackedApk != null ? newRevisionHackedApk.getContents() : null);
        revisionsModel.reset();
        LOGGER.debug("revision saved");

        ProjectDTO updatedProject = projectService.findById(projectsModel.getSelectedProject().getId());
        projectsModel.setSelectedProject(updatedProject);
        revisionsModel.setRevisionsListDataModel(new RevisionsListDataModel(updatedProject.getRevisions()));
        return Navigation.SAME_PAGE.getView();
    }

    public void prepareRevisionView() throws IOException, WriterException {
        RevisionDTO selectedRevisionDTO = revisionsModel.getSelectedRevisionDTO();
        if(selectedRevisionDTO != null){
            updateQRCodes(selectedRevisionDTO);
            LOGGER.debug(selectedRevisionDTO.getId() + " revision preparing finished");
        }
        else {
            LOGGER.error("revision not selected");
        }
    }

    public void onRevisionSelected(SelectEvent event){
        RevisionDTO revisionDTO = (RevisionDTO) event.getObject();
        revisionsModel.setSelectedRevisionDTO(revisionDTO);
        LOGGER.debug(revisionDTO.getId() + " revision selected");
        redirectTo(Navigation.REVISION);
    }

    public String uploadApkToRevision() throws IOException, WriterException {
        UploadedFile newRevisionCleanApk = revisionsModel.getUploadCleanApk();
        UploadedFile newRevisionHackedApk = revisionsModel.getUploadHackedApk();
        RevisionDTO updatedRevision = revisionService.updateRevisionPackages(revisionsModel.getSelectedRevisionDTO(),
                newRevisionCleanApk != null ? newRevisionCleanApk.getContents() : null,
                newRevisionHackedApk != null ? newRevisionHackedApk.getContents() : null);
        revisionsModel.setSelectedRevisionDTO(updatedRevision);
        updateQRCodes(updatedRevision);
        LOGGER.debug("revision updated");
        return Navigation.SAME_PAGE.getView();
    }

    private void updateQRCodes(RevisionDTO revisionDTO) throws IOException, WriterException {
        revisionsModel.setCleanApkQRCode(getQRCodeFromUrl(revisionDTO.getMainPackageUrl()));
        revisionsModel.setHackedApkQRCode(getQRCodeFromUrl(revisionDTO.getSpecialPackageUrl()));
    }

    private StreamedContent getQRCodeFromUrl(String url) throws WriterException, IOException {
        if (StringUtils.isNotEmpty(url)) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            Writer writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(url,
                    BarcodeFormat.QR_CODE, 100, 100);
            MatrixToImageWriter.writeToStream(matrix, "PNG", buf);
            byte[] bytes = buf.toByteArray();
            LOGGER.debug("qrcode for " + url + " generated");
            return new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/png");
        } else {
            LOGGER.debug("empty url");
            return null;
        }
    }
}
