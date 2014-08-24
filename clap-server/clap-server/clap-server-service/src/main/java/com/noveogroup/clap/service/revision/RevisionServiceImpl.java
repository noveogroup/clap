package com.noveogroup.clap.service.revision;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.RevisionConverter;
import com.noveogroup.clap.converter.RevisionVariantConverter;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.RevisionVariantDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.message.ScreenshotMessageEntity;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.exception.WrapException;
import com.noveogroup.clap.model.file.FileType;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionVariant;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @EJB
    private RevisionVariantDAO revisionVariantDAO;

    @Inject
    private UrlService urlService;

    @Inject
    private FileService fileService;

    @Inject
    private UserService userService;

    @EJB
    private UserDAO userDAO;

    @Inject
    private RevisionVariantConverter revisionVariantConverter;

    @RequiresAuthentication
    @WrapException
    @Override
    public boolean createOrUpdateRevision(final @NotNull CreateOrUpdateRevisionRequest request) {
        boolean ret = false;
        ProjectEntity projectEntity = projectDAO.findProjectByExternalIdOrReturnNull(request.getProjectExternalId());
        if (projectEntity == null) {
            projectEntity = new ProjectEntity();
            projectEntity.setExternalId(request.getProjectExternalId());
            projectEntity.setName(request.getProjectExternalId());
            projectEntity.setDescription("Description empty");
            projectEntity.setCreationDate(new Date());
            projectEntity = projectDAO.persist(projectEntity);
        }
        RevisionEntity revisionEntity = revisionDAO.getRevisionByHashOrNull(request.getRevisionHash());
        boolean needToCheckRevisionsAmount = false;
        if (revisionEntity == null) {
            revisionEntity = new RevisionEntity();
            revisionEntity.setHash(request.getRevisionHash());
            revisionEntity.setTimestamp(new Date().getTime());
            revisionEntity.setRevisionType(RevisionType.DEVELOP);
            needToCheckRevisionsAmount = true;
            ret = true;
            revisionEntity.setProject(projectEntity);
        }
        revisionDAO.persist(revisionEntity);
        if (needToCheckRevisionsAmount) {
            projectEntity.getRevisions().add(revisionEntity);
            checkAndRemoveOldRevisions(projectEntity);
        }
        RevisionVariantEntity revisionVariantEntity = revisionVariantDAO.getRevisionByHash(request.getVariantHash());
        if (revisionVariantEntity == null) {
            revisionVariantEntity = new RevisionVariantEntity();
            revisionVariantEntity.setFullHash(request.getVariantHash());
            ret = true;
            List<RevisionVariantEntity> variants = revisionEntity.getVariants();
            if (variants == null) {
                variants = Lists.newArrayList();
                revisionEntity.setVariants(variants);
            }
            variants.add(revisionVariantEntity);
            revisionVariantEntity.setRevision(revisionEntity);
        }
        revisionVariantEntity.setPackageVariant(request.getVariantName());
        revisionVariantEntity.setRandom(request.getRandom());

        processPackages(revisionVariantEntity, request);
        revisionVariantDAO.persist(revisionVariantEntity);
        revisionVariantDAO.flush();
        return ret;
    }

    @Override
    public void updateRevisionData(final Revision revision) {
        final RevisionEntity revisionByHash = revisionDAO.getRevisionByHash(revision.getHash());
        revisionConverter.updateRevisionData(revisionByHash, revision);
    }

    @RequiresAuthentication
    @Override
    public ApplicationFile getApplication(final Long revisionId, final Long variantId) {
        final RevisionVariantEntity revisionVariantEntity = revisionVariantDAO.findById(variantId);
        if (revisionVariantEntity != null) {
            final ApplicationFile ret = new ApplicationFile();
            ret.setContent(fileService.getFile(revisionVariantEntity.getPackageFileUrl()));
            ret.setFilename(createFileName(revisionVariantEntity.getRevision().getProject(),
                    revisionVariantEntity.getPackageVariant()));
            return ret;
        }
        return null;
    }

    @RequiresAuthentication
    @Override
    public Revision getRevision(final Long revisionId) {
        final RevisionEntity revisionEntity = revisionDAO.findById(revisionId);
        final Revision revision = revisionConverter.map(revisionEntity, true, configBean);
        createUrls(revision, revisionEntity);
        return revision;
    }

    @RequiresAuthentication
    @Override
    public RevisionWithApkStructure getRevisionWithApkStructure(final Long revisionId) {
        final RevisionEntity revisionEntity = revisionDAO.findById(revisionId);
        final RevisionWithApkStructure revision = revisionConverter.mapWithApkStructure(revisionEntity,
                true, configBean);
        createUrls(revision, revisionEntity);
        return revision;
    }

    @RequiresAuthentication
    @RequiresPermissions("DELETE_REVISIONS")
    @WrapException
    @Override
    public void deleteRevision(final Long id) {
        final RevisionEntity revisionEntity = revisionDAO.findById(id);
        List<String> filesToDelete = Lists.newArrayList();
        for (RevisionVariantEntity variantEntity : revisionEntity.getVariants()) {
            filesToDelete.add(variantEntity.getPackageFileUrl());
        }
        markRevisionScreenshotsToDelete(revisionEntity, filesToDelete);
        revisionDAO.removeById(id);
        revisionDAO.flush();
        for (String fileToDelete : filesToDelete) {
            fileService.removeFile(fileToDelete);
        }
    }

    private void markRevisionScreenshotsToDelete(final RevisionEntity revisionEntity,
                                                 final List<String> filesToDelete) {
        for (RevisionVariantEntity revisionVariantEntity : revisionEntity.getVariants()) {
            final List<BaseMessageEntity> messages = revisionVariantEntity.getMessages();
            if (CollectionUtils.isNotEmpty(messages)) {
                for (BaseMessageEntity messageEntity : messages) {
                    if (messageEntity instanceof ScreenshotMessageEntity) {
                        filesToDelete.add(((ScreenshotMessageEntity) messageEntity).getScreenshotFileUrl());
                    }
                }
            }
        }
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

    @Override
    public RevisionVariantWithApkStructure getRevisionVariantWithApkStructure(final Long variantId) {
        final RevisionVariantEntity entity = revisionVariantDAO.findById(variantId);
        return revisionVariantConverter.mapWithApkStructure(entity, configBean);
    }

    /**
     * processes uploaded packages
     * makes copy in temp files dir for writing to DB
     * sets boolean flags in revision entity
     *
     * @param revisionVariantEntity to modify flags
     * @param request               request object, updates stream references in it
     */
    private void processPackages(final RevisionVariantEntity revisionVariantEntity,
                                 final CreateOrUpdateRevisionRequest request) {
        String currentUserLogin = null;
        UserEntity userByLogin = null;
        final InputStream packageStream = request.getPackageStream();
        if (packageStream != null) {
            currentUserLogin = userService.getCurrentUserLogin();
            userByLogin = userDAO.getUserByLogin(currentUserLogin);
            processStreamedPackage(revisionVariantEntity, userByLogin, packageStream);
        }
    }

    private void processStreamedPackage(final RevisionVariantEntity revisionVariantEntity,
                                        final UserEntity uploadedBy,
                                        final InputStream streamedPackage) {
        try {
            final File file = fileService.saveFile(FileType.APK, streamedPackage, "clap_apk_");
            final ApkInfoMainExtractor mainExtractor = new ApkInfoMainExtractor(file);
            final IconExtractor iconExtractor = new IconExtractor();
            mainExtractor.addInfoExtractor(iconExtractor);
            mainExtractor.processApk();
            final byte[] icon = iconExtractor.getIcon();
            if (icon != null) {
                final File iconFile = fileService.saveFile(FileType.PROJECT_ICON, new ByteArrayInputStream(icon));
                revisionVariantEntity.getRevision().getProject().setIconFilePath(iconFile.getAbsolutePath());
            }
            final ApkStructure apkStructure = mainExtractor.getStructure();
            revisionVariantEntity.setApkStructureJSON(new Gson().toJson(apkStructure, ApkStructure.class));
            revisionVariantEntity.setPackageFileUrl(file.getAbsolutePath());
            revisionVariantEntity.setPackageUploadedBy(uploadedBy);
        } catch (IOException e) {
            LOGGER.error("error while processing: " + streamedPackage, e);
        }
    }

    private void createUrls(final Revision outcomeRevision, final RevisionEntity revisionEntity) {
        final UserWithPersistedAuth userWithToken = userService.getUserWithToken();
        final List<RevisionVariantEntity> variants = revisionEntity.getVariants();
        if (variants != null) {
            final String token = userWithToken.getToken();
            for (final RevisionVariant variant : outcomeRevision.getVariants()) {
                variant.setPackageUrl(urlService.createUrl(outcomeRevision.getId(), variant.getId(), token));
            }
        }
    }

    private String createFileName(final ProjectEntity projectEntity, final String variantName) {
        return projectEntity.getName() + "_" + variantName + ".apk";
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
            if (revisionToRemove != null) {
                for (RevisionVariantEntity variantEntity : revisionToRemove.getVariants()) {
                    fileService.removeFile(variantEntity.getPackageFileUrl());
                }
            }
            revisionDAO.remove(revisionToRemove);
        }
    }
}
