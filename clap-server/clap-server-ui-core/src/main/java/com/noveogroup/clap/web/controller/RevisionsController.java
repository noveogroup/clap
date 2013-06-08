package com.noveogroup.clap.web.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noveogroup.clap.facade.ProjectsFacade;
import com.noveogroup.clap.facade.RevisionsFacade;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.StreamedPackage;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.apk.ApkInfoExtractorFactory;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Named
@RequestScoped
public class RevisionsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionsController.class);
    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    @Inject
    private RevisionsFacade revisionsFacade;

    @Inject
    private ProjectsFacade projectsFacade;

    public String saveNewRevision() throws IOException {
        LOGGER.debug("saving new revision");
        final Project project = projectsModel.getSelectedProject();
        final Revision revision = new Revision();
        revision.setRevisionType(RevisionType.DEVELOP);
        revision.setTimestamp(new Date().getTime());
        final UploadedFile newRevisionCleanApk = revisionsModel.getUploadCleanApk();
        final UploadedFile newRevisionHackedApk = revisionsModel.getUploadHackedApk();

        final AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        request.setProjectExternalId(project.getExternalId());
        request.setRevision(revision);
        if (newRevisionCleanApk != null) {
            request.setMainPackage(
                    new StreamedPackage(
                            newRevisionCleanApk.getInputstream(),
                            newRevisionCleanApk.getSize()));
        }
        if (newRevisionHackedApk != null) {
            request.setSpecialPackage(
                    new StreamedPackage(
                            newRevisionHackedApk.getInputstream(),
                            newRevisionHackedApk.getSize()));
        }
        revisionsFacade.addOrGetRevision(request);

        revisionsModel.reset();
        LOGGER.debug("revision saved");
        final Project updatedProject = projectsFacade.findById(projectsModel.getSelectedProject().getId());
        if (updatedProject != null) {
            projectsModel.setSelectedProject(updatedProject);
            revisionsModel.setRevisionsListDataModel(new RevisionsListDataModel(updatedProject.getRevisions()));
        }
        return Navigation.SAME_PAGE.getView();
    }

    public void prepareRevisionView() throws IOException, WriterException {
        final Revision selectedRevision = revisionsModel.getSelectedRevision();
        if (selectedRevision != null) {
            final RevisionRequest request = new RevisionRequest();
            request.setAuthentication(new Authentication());
            request.setRevisionId(selectedRevision.getId());
            revisionsModel.setSelectedRevision(revisionsFacade.getRevision(request));
            updateQRCodes(revisionsModel.getSelectedRevision());
            LOGGER.debug(selectedRevision.getId() + " revision preparing finished");
        } else {
            LOGGER.error("revision not selected");
        }
    }

    public void onRevisionSelected(final SelectEvent event) {
        final Revision revision = (Revision) event.getObject();
        revisionsModel.setSelectedRevision(revision);
        LOGGER.debug(revision.getId() + " revision selected");
        redirectTo(Navigation.REVISION);
    }

    public String uploadApkToRevision() throws IOException, WriterException {
        final UploadedFile newRevisionCleanApk = revisionsModel.getUploadCleanApk();
        final UploadedFile newRevisionHackedApk = revisionsModel.getUploadHackedApk();
        final UpdateRevisionPackagesRequest request = new UpdateRevisionPackagesRequest();
        request.setRevisionHash(revisionsModel.getSelectedRevision().getHash());
        if (newRevisionCleanApk != null) {
            request.setMainPackage(
                    new StreamedPackage(
                            newRevisionCleanApk.getInputstream(),
                            newRevisionCleanApk.getSize()));
        }
        if (newRevisionHackedApk != null) {
            request.setSpecialPackage(
                    new StreamedPackage(
                            newRevisionHackedApk.getInputstream(),
                            newRevisionHackedApk.getSize()));
        }
        final Revision updatedRevision = revisionsFacade.updateRevisionPackages(request);
        revisionsModel.setSelectedRevision(updatedRevision);
        updateQRCodes(updatedRevision);
        LOGGER.debug("revision updated");
        return Navigation.SAME_PAGE.getView();
    }

    private void updateQRCodes(final Revision revision) throws IOException, WriterException {
        if (revision != null) {
            revisionsModel.setCleanApkQRCode(getQRCodeFromUrl(revision.getMainPackageUrl()));
            revisionsModel.setHackedApkQRCode(getQRCodeFromUrl(revision.getSpecialPackageUrl()));
        }
    }

    private StreamedContent getQRCodeFromUrl(final String url) throws WriterException, IOException {
        if (StringUtils.isNotEmpty(url)) {
            final ByteArrayOutputStream buf = new ByteArrayOutputStream();
            final Writer writer = new QRCodeWriter();
            final BitMatrix matrix = writer.encode(url,
                    BarcodeFormat.QR_CODE, 100, 100);
            MatrixToImageWriter.writeToStream(matrix, "PNG", buf);
            final byte[] bytes = buf.toByteArray();
            LOGGER.debug("qrcode for " + url + " generated");
            return new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/png");
        } else {
            LOGGER.debug("empty url");
            return null;
        }
    }

    private void test(byte[] apk) {
        if (apk != null) {
            try {
                ApkInfoExtractorFactory apkInfoExtractorFactory = ApkInfoExtractorFactory.getInstance(apk);
                apkInfoExtractorFactory.createIconExtractor().getIcon();
            } catch (FileNotFoundException e) {
                LOGGER.error("", e);
            }
        }
    }
}
