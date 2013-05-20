package com.noveogroup.clap.web.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
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
        Project project = projectsModel.getSelectedProject();
        Revision revision = new Revision();
        revision.setRevisionType(RevisionType.DEVELOP);
        revision.setTimestamp(new Date().getTime());
        UploadedFile newRevisionCleanApk = revisionsModel.getUploadCleanApk();
        UploadedFile newRevisionHackedApk = revisionsModel.getUploadHackedApk();

        AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        request.setProjectExternalId(project.getName());
        request.setRevision(revision);
        if(newRevisionCleanApk != null){
            request.setMainPackage(newRevisionCleanApk.getContents());
        }
        if(newRevisionHackedApk != null){
            request.setSpecialPackage(newRevisionHackedApk.getContents());
        }
        revisionService.addOrGetRevision(request);

        revisionsModel.reset();
        LOGGER.debug("revision saved");
        //TODO authentication
        Project updatedProject = projectService.findById(null,projectsModel.getSelectedProject().getId());
        projectsModel.setSelectedProject(updatedProject);
        revisionsModel.setRevisionsListDataModel(new RevisionsListDataModel(updatedProject.getRevisions()));
        return Navigation.SAME_PAGE.getView();
    }

    public void prepareRevisionView() throws IOException, WriterException {
        Revision selectedRevision = revisionsModel.getSelectedRevision();
        if(selectedRevision != null){
            RevisionRequest request = new RevisionRequest();
            request.setRevisionId(selectedRevision.getId());
            //TODO authentication
            revisionsModel.setSelectedRevision(revisionService.getRevision(request));
            updateQRCodes(revisionsModel.getSelectedRevision());
            LOGGER.debug(selectedRevision.getId() + " revision preparing finished");
        }
        else {
            LOGGER.error("revision not selected");
        }
    }

    public void onRevisionSelected(SelectEvent event){
        Revision revision = (Revision) event.getObject();
        revisionsModel.setSelectedRevision(revision);
        LOGGER.debug(revision.getId() + " revision selected");
        redirectTo(Navigation.REVISION);
    }

    public String uploadApkToRevision() throws IOException, WriterException {
        UploadedFile newRevisionCleanApk = revisionsModel.getUploadCleanApk();
        UploadedFile newRevisionHackedApk = revisionsModel.getUploadHackedApk();
        UpdateRevisionPackagesRequest request = new UpdateRevisionPackagesRequest();
        request.setRevisionId(revisionsModel.getSelectedRevision().getId());
        if(newRevisionCleanApk != null){
            request.setMainPackage(newRevisionCleanApk.getContents());
        }
        if(newRevisionHackedApk != null){
            request.setSpecialPackage(newRevisionHackedApk.getContents());
        }
        Revision updatedRevision = revisionService.updateRevisionPackages(request);
        revisionsModel.setSelectedRevision(updatedRevision);
        updateQRCodes(updatedRevision);
        LOGGER.debug("revision updated");
        return Navigation.SAME_PAGE.getView();
    }

    private void updateQRCodes(Revision revision) throws IOException, WriterException {
        revisionsModel.setCleanApkQRCode(getQRCodeFromUrl(revision.getMainPackageUrl()));
        revisionsModel.setHackedApkQRCode(getQRCodeFromUrl(revision.getSpecialPackageUrl()));
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
