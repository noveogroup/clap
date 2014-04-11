package com.noveogroup.clap.service.revision;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.noveogroup.clap.config.ConfigBean;
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
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import com.noveogroup.clap.model.revision.StreamedPackage;
import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.model.user.UserWithPersistedAuth;
import com.noveogroup.clap.service.apk.ApkInfoMainExtractor;
import com.noveogroup.clap.service.apk.IconExtractor;
import com.noveogroup.clap.service.file.FileService;
import com.noveogroup.clap.service.url.UrlService;
import com.noveogroup.clap.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Inner revision service
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RevisionServiceImpl implements RevisionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionServiceImpl.class);

    @Inject
    private ConfigBean configBean;

    @Inject
    private RevisionConverter revisionConverter;

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @Inject
    private UrlService urlService;

    @Inject
    private FileService fileService;

    @Inject
    private UserService userService;

    @EJB
    private UserDAO userDAO;

    @RequiresAuthentication
    @WrapException
    @Override
    public Revision addOrGetRevision(final @NotNull CreateOrUpdateRevisionRequest request) {
        RevisionEntity revisionEntity = revisionDAO.getRevisionByHashOrNull(request.getRevisionHash());
        boolean needToCheckRevisionsAmount = false;
        if (revisionEntity == null) {
            revisionEntity = new RevisionEntity();
            revisionEntity.setHash(request.getRevisionHash());
            needToCheckRevisionsAmount = true;
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
        } else if (needToCheckRevisionsAmount) {
            checkAndRemoveOldRevisions(projectEntity);
        }
        revisionEntity.setProject(projectEntity);
        projectEntity.getRevisions().add(revisionEntity);

        processPackages(revisionEntity, request);
        revisionEntity = revisionDAO.persist(revisionEntity);
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

    @Override
    public void updateRevisionData(final Revision revision) {
        final RevisionEntity revisionByHash = revisionDAO.getRevisionByHash(revision.getHash());
        revisionConverter.updateRevisionData(revisionByHash, revision);
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
                        ret.setContent(fileService.createTempFile(
                                revisionEntity.getMainPackage().getBinaryStream()));
                        ret.setFilename(createFileName(revisionEntity.getProject(), true));
                        return ret;
                    case SPECIAL:
                        ret.setContent(fileService.createTempFile(
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

    @RequiresAuthentication
    @RequiresPermissions("DELETE_REVISIONS")
    @WrapException
    @Override
    public void deleteRevision(final Long id) {
        revisionDAO.removeById(id);
        revisionDAO.flush();
    }

    @Override
    public Set<RevisionType> getAvailableTypesToChange(final User user, final Revision revision) {
        final Set<RevisionType> revisionTypes = Sets.newLinkedHashSet();
        for (RevisionType type : RevisionType.values()) {
            //TODO think about more good solution
            if (user.getClapPermissions().contains(ClapPermission.parseName("SWITCH_REVISION_TO_" + type.name()))) {
                revisionTypes.add(type);
            }
        }
        return revisionTypes;
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
    private void processPackages(final RevisionEntity revisionEntity, final CreateOrUpdateRevisionRequest request) {
        String currentUserLogin = null;
        UserEntity userByLogin = null;
        final InputStream mainPackage = request.getMainPackage();
        final InputStream specialPackage = request.getSpecialPackage();
        boolean extractInfo = true;
        if (mainPackage != null || specialPackage != null) {
            currentUserLogin = userService.getCurrentUserLogin();
            userByLogin = userDAO.getUserByLogin(currentUserLogin);
        }
        if (mainPackage != null) {
            extractInfo = !processStreamedPackage(revisionEntity, mainPackage, extractInfo,true);
            revisionEntity.setMainPackageUploadedBy(userByLogin);
        }
        if (specialPackage != null) {
            processStreamedPackage(revisionEntity, specialPackage, extractInfo,false);
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
                                           final InputStream streamedPackage,
                                           final boolean extractInfo,final boolean isMainPackage) {
        boolean ret = false;
        try {
            final File file = fileService.saveFile(streamedPackage);
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
            if(isMainPackage){
                revisionEntity.setMainPackageFileUrl(file.getAbsolutePath());
            } else {
                revisionEntity.setSpecialPackageFileUrl(file.getAbsolutePath());
            }
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

    private void checkAndRemoveOldRevisions(final ProjectEntity projectEntity) {
        final List<RevisionEntity> revisions = revisionDAO.findForProjectAndType(projectEntity.getId(),
                RevisionType.DEVELOP);
        if (CollectionUtils.isNotEmpty(revisions) && configBean.getKeepDevRevisions() <= revisions.size()) {
            Collections.sort(revisions, new Comparator<RevisionEntity>() {
                @Override
                public int compare(final RevisionEntity lhs, final RevisionEntity rhs) {
                    return (int) (lhs.getTimestamp() - rhs.getTimestamp());
                }
            });
            final RevisionEntity revisionToRemove = revisions.get(0);
            fileService.removeFile(revisionToRemove.getMainPackageFileUrl());
            fileService.removeFile(revisionToRemove.getSpecialPackageFileUrl());
            revisionDAO.remove(revisionToRemove);
        }
    }
}
