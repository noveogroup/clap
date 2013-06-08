package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.converter.RevisionConverter;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.BaseRevisionPackagesRequest;
import com.noveogroup.clap.model.request.revision.GetApplicationRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.StreamedPackage;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.tempfiles.TempFileService;
import com.noveogroup.clap.service.url.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author Mikhail Demidov
 */
@Stateless
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
        processPackages(revisionEntity, request);
        ProjectEntity projectEntity = null;

        projectEntity = projectDAO.findProjectByExternalIdOrReturnNull(request.getProjectExternalId());
        if (projectEntity == null) {
            projectEntity = new ProjectEntity();
            projectEntity.setExternalId(request.getProjectExternalId());
            projectEntity.setName("Name");
            projectEntity.setDescription("Description");
            projectEntity.setCreationDate(new Date());
            projectEntity = projectDAO.persist(projectEntity);
        }
        revisionEntity.setProject(projectEntity);
        projectEntity.getRevisions().add(revisionEntity);

        projectDAO.persist(projectEntity);
        revisionEntity = revisionDAO.persist(revisionEntity,request.getMainPackage(),request.getSpecialPackage());
        final Revision outcomeRevision = revisionConverter.map(revisionEntity);
        outcomeRevision.setProjectId(projectEntity.getId());
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    @Override
    public Revision updateRevisionPackages(final @NotNull UpdateRevisionPackagesRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.getRevisionByHash(request.getRevisionHash());
        return updateRevisionPackages(revisionEntity, request);
    }

    @Override
    public ApplicationFile getApplication(final GetApplicationRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.findById(request.getRevisionId());
        if (revisionEntity != null) {
            final ApplicationFile ret = new ApplicationFile();
            try {
            switch (request.getApplicationType()) {
                case MAIN:
                    ret.setContent(tempFileService.createTempFile(
                            revisionEntity.getMainPackage().getBinaryStream()));
                    ret.setFilename(createFileName(revisionEntity.getProject(), false));
                    return ret;
                case SPECIAL:
                    ret.setContent(tempFileService.createTempFile(
                            revisionEntity.getSpecialPackage().getBinaryStream()));
                    ret.setFilename(createFileName(revisionEntity.getProject(), true));
                    return ret;
                default:
                    throw new IllegalArgumentException("unknown application type : " + request.getApplicationType());
            }
            } catch (IOException e) {
                LOGGER.error("error getting file",e);
            } catch (SQLException e) {
                LOGGER.error("error getting file",e);
            }
        }
        return null;
    }

    @Override
    public Revision getRevision(final RevisionRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.findById(request.getRevisionId());
        final Revision revision = revisionConverter.map(revisionEntity);
        createUrls(revision, revisionEntity);
        return revision;
    }


    private Revision updateRevisionPackages(RevisionEntity revisionEntity, final BaseRevisionPackagesRequest request) {

        processPackages(revisionEntity, request);
        revisionEntity = revisionDAO.persist(revisionEntity,request.getMainPackage(),request.getSpecialPackage());
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
     * @param request request object, updates stream references in it
     */
    private void processPackages(final RevisionEntity revisionEntity, final BaseRevisionPackagesRequest request) {
        final StreamedPackage mainPackage = request.getMainPackage();
        if (mainPackage != null) {
            processStreamedPackage(mainPackage);
            revisionEntity.setMainPackageLoaded(true);
        }
        final StreamedPackage specialPackage = request.getSpecialPackage();
        if (specialPackage != null) {
            processStreamedPackage(specialPackage);
            revisionEntity.setSpecialPackageLoaded(true);
        }
    }

    private void processStreamedPackage(final StreamedPackage streamedPackage) {
        try {
            final File file = tempFileService.createTempFile(streamedPackage.getStream());
            streamedPackage.setStream(new FileInputStream(file));
        } catch (IOException e) {
            LOGGER.error("error while processing: "+streamedPackage,e);
        }
    }

    private void createUrls(final Revision outcomeRevision, final RevisionEntity revisionEntity) {
        if (revisionEntity.isMainPackageLoaded()) {
            outcomeRevision.setMainPackageUrl(urlService.createUrl(outcomeRevision.getId(), true));
        }
        if (revisionEntity.isSpecialPackageLoaded()) {
            outcomeRevision.setSpecialPackageUrl(urlService.createUrl(outcomeRevision.getId(), false));
        }
    }

    private String createFileName(final ProjectEntity projectEntity, final boolean mainPackage) {
        return projectEntity.getName() + (mainPackage ? "" : "_hacked") + ".apk";
    }
}
