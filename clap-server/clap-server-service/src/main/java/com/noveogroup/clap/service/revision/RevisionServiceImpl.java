package com.noveogroup.clap.service.revision;

import com.google.gson.Gson;
import com.noveogroup.clap.converter.RevisionConverter;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.BaseRevisionPackagesRequest;
import com.noveogroup.clap.model.request.revision.StreamedPackage;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.service.apk.ApkInfoMainExtractor;
import com.noveogroup.clap.service.apk.IconExtractor;
import com.noveogroup.clap.service.tempfiles.TempFileService;
import com.noveogroup.clap.service.url.UrlService;
import com.noveogroup.clap.service.user.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Inner revision service
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RevisionServiceImpl implements RevisionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionServiceImpl.class);

    @Inject
    private RevisionConverter revisionConverter;

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @Inject
    private UrlService urlService;

    @Inject
    private TempFileService tempFileService;

    @Inject
    private UserService userService;

    @EJB
    private UserDAO userDAO;

    @RequiresAuthentication
    @WrapException
    @Override
    public Revision addOrGetRevision(final @NotNull AddOrGetRevisionRequest request) {
        final Revision revision = request.getRevision();
        RevisionEntity revisionEntity = revisionDAO.getRevisionByHashOrNull(request.getRevision().getHash());
        if (revisionEntity == null) {
            revisionEntity = revisionConverter.map(revision);
        }
        if (revisionEntity.getTimestamp() == null) {
            revisionEntity.setTimestamp(new Date().getTime());
        }
        if (revisionEntity.getRevisionType() == null) {
            revisionEntity.setRevisionType(RevisionType.DEVELOP);
        }
        ProjectEntity projectEntity = null;

        projectEntity = projectDAO.findProjectByExternalIdOrReturnNull(request.getProjectExternalId());
        if (projectEntity == null) {
            projectEntity = new ProjectEntity();
            projectEntity.setExternalId(request.getProjectExternalId());
            projectEntity.setName(request.getProjectExternalId());
            projectEntity.setDescription("Description empty");
            projectEntity.setCreationDate(new Date());
            projectEntity = projectDAO.persist(projectEntity);
        }
        revisionEntity.setProject(projectEntity);
        projectEntity.getRevisions().add(revisionEntity);

        processPackages(revisionEntity, request);
        projectDAO.persist(projectEntity);
        revisionEntity = revisionDAO.persist(revisionEntity, request.getMainPackage(), request.getSpecialPackage());
        projectDAO.flush();
        revisionDAO.flush();
        final Revision outcomeRevision = revisionConverter.map(revisionEntity);
        outcomeRevision.setProjectId(projectEntity.getId());
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    @RequiresAuthentication
    @WrapException
    @Override
    public Revision updateRevisionPackages(final @NotNull UpdateRevisionPackagesRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.getRevisionByHash(request.getRevisionHash());
        return updateRevisionPackages(revisionEntity, request);
    }

    @RequiresAuthentication
    @Override
    public ApplicationFile getApplication(final Long revisionId, final ApplicationType applicationType) {
        final RevisionEntity revisionEntity = revisionDAO.findById(revisionId);
        if (revisionEntity != null) {
            final ApplicationFile ret = new ApplicationFile();
            try {
                switch (applicationType) {
                    case MAIN:
                        ret.setContent(tempFileService.createTempFile(
                                revisionEntity.getMainPackage().getBinaryStream()));
                        ret.setFilename(createFileName(revisionEntity.getProject(), true));
                        return ret;
                    case SPECIAL:
                        ret.setContent(tempFileService.createTempFile(
                                revisionEntity.getSpecialPackage().getBinaryStream()));
                        ret.setFilename(createFileName(revisionEntity.getProject(), false));
                        return ret;
                    default:
                        throw new IllegalArgumentException("unknown application type : "
                                + applicationType);
                }
            } catch (SQLException e) {
                LOGGER.error("error getting file", e);
            }
        }
        return null;
    }

    @RequiresAuthentication
    @Override
    public Revision getRevision(final Long revisionId) {
        final RevisionEntity revisionEntity = revisionDAO.findById(revisionId);
        final Revision revision = revisionConverter.map(revisionEntity);
        createUrls(revision, revisionEntity);
        return revision;
    }

    @RequiresAuthentication
    @Override
    public RevisionWithApkStructure getRevisionWithApkStructure(final Long revisionId) {
        final RevisionEntity revisionEntity = revisionDAO.findById(revisionId);
        final RevisionWithApkStructure revision = revisionConverter.mapWithApkStructure(revisionEntity);
        createUrls(revision, revisionEntity);
        return revision;
    }

    private Revision updateRevisionPackages(RevisionEntity revisionEntity, final BaseRevisionPackagesRequest request) {
        processPackages(revisionEntity, request);
        revisionEntity = revisionDAO.persist(revisionEntity, request.getMainPackage(), request.getSpecialPackage());
        revisionDAO.flush();
        final Revision outcomeRevision = revisionConverter.map(revisionEntity);
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    /**
     * processes uploaded packages
     * makes copy in temp files dir for writing to DB
     * sets boolean flags in revision entity
     *
     * @param revisionEntity to modify flags
     * @param request        request object, updates stream references in it
     */
    private void processPackages(final RevisionEntity revisionEntity, final BaseRevisionPackagesRequest request) {
        String currentUserLogin = null;
        UserEntity userByLogin = null;
        final StreamedPackage mainPackage = request.getMainPackage();
        final StreamedPackage specialPackage = request.getSpecialPackage();
        boolean extractInfo = true;
        if (mainPackage != null || specialPackage != null) {
            currentUserLogin = userService.getCurrentUserLogin();
            userByLogin = userDAO.getUserByLogin(currentUserLogin);
        }
        if (mainPackage != null) {
            extractInfo = !processStreamedPackage(revisionEntity, mainPackage, extractInfo);
            revisionEntity.setMainPackageLoaded(true);
            revisionEntity.setMainPackageUploadedBy(userByLogin);
        }
        if (specialPackage != null) {
            processStreamedPackage(revisionEntity, specialPackage, extractInfo);
            revisionEntity.setSpecialPackageLoaded(true);
            revisionEntity.setSpecialPackageUploadedBy(userByLogin);
        }
    }

    /**
     * @param revisionEntity
     * @param streamedPackage
     * @param extractInfo     true if need to extract apk info (icon, structure, etc)
     * @return true if info was extracted
     */
    private boolean processStreamedPackage(final RevisionEntity revisionEntity,
                                           final StreamedPackage streamedPackage,
                                           final boolean extractInfo) {
        boolean ret = false;
        try {
            final File file = tempFileService.createTempFile(streamedPackage.getStream());
            if (extractInfo) {
                final ApkInfoMainExtractor mainExtractor = new ApkInfoMainExtractor(file);
                final IconExtractor iconExtractor = new IconExtractor();
                mainExtractor.addInfoExtractor(iconExtractor);
                mainExtractor.processApk();
                revisionEntity.getProject().setIconFile(iconExtractor.getIcon());
                final ApkStructure apkStructure = mainExtractor.getStructure();
                revisionEntity.setApkStructureJSON(new Gson().toJson(apkStructure, ApkStructure.class));
                ret = true;
            }
            streamedPackage.setStream(new FileInputStream(file));
        } catch (IOException e) {
            LOGGER.error("error while processing: " + streamedPackage, e);
        }
        return ret;
    }

    private void createUrls(final Revision outcomeRevision, final RevisionEntity revisionEntity) {
        final UserWithPersistedAuth userWithToken = userService.getUserWithToken();
        if (revisionEntity.isMainPackageLoaded()) {
            outcomeRevision.setMainPackageUrl(urlService.createUrl(outcomeRevision.getId(), true,
                    userWithToken.getToken()));
        }
        if (revisionEntity.isSpecialPackageLoaded()) {
            outcomeRevision.setSpecialPackageUrl(urlService.createUrl(outcomeRevision.getId(), false,
                    userWithToken.getToken()));
        }
    }

    private String createFileName(final ProjectEntity projectEntity, final boolean mainPackage) {
        return projectEntity.getName() + (mainPackage ? "" : "_hacked") + ".apk";
    }
}
