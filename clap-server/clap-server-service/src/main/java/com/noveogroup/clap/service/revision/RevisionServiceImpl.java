package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.auth.AuthenticationRequired;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionType;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.GetApplicationRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.url.UrlService;
import com.noveogroup.clap.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Mikhail Demidov
 */
@Stateless
@Interceptors({ClapMainInterceptor.class})
@TransactionManagement(TransactionManagementType.BEAN)
public class RevisionServiceImpl implements RevisionService {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @Inject
    private UrlService urlService;


    @Transactional
    @Override
    public Revision addOrGetRevision(final @NotNull AddOrGetRevisionRequest request) {
        final Revision revision = request.getRevision();
        RevisionEntity revisionEntity = MAPPER.map(revision, RevisionEntity.class);
        if (revisionEntity.getTimestamp() == null) {
            revisionEntity.setTimestamp(new Date().getTime());
        }
        if (revisionEntity.getRevisionType() == null) {
            revisionEntity.setRevisionType(RevisionType.DEVELOP);
        }
        addPackages(revisionEntity, request.getMainPackage(), request.getSpecialPackage());
        ProjectEntity projectEntity = null;
        try {
            projectEntity = projectDAO.findProjectByExternalId(request.getProjectExternalId());
        } catch (NoResultException e) {

        }
        if (projectEntity == null) {
            projectEntity = new ProjectEntity();
            projectEntity.setExternalId(request.getProjectExternalId());
            projectEntity = projectDAO.persist(projectEntity);
        }
        revisionEntity.setProject(projectEntity);
        projectEntity.getRevisions().add(revisionEntity);

        projectDAO.persist(projectEntity);
        revisionEntity = revisionDAO.persist(revisionEntity);
        final Revision outcomeRevision = MAPPER.map(revisionEntity, Revision.class);
        outcomeRevision.setProjectId(projectEntity.getId());
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    @Transactional
    @Override
    public Revision updateRevisionPackages(final @NotNull UpdateRevisionPackagesRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.getRevisionByHash(request.getRevisionHash());
        return updateRevisionPackages(revisionEntity, request.getMainPackage(), request.getSpecialPackage());
    }

    @Transactional
    @AuthenticationRequired
    @Override
    public ApplicationFile getApplication(final GetApplicationRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.findById(request.getRevisionId());
        if (revisionEntity != null) {
            final ApplicationFile ret = new ApplicationFile();
            switch (request.getApplicationType()) {
                case MAIN:
                    ret.setContent(revisionEntity.getSpecialPackage());
                    ret.setFilename(createFileName(revisionEntity.getProject(), false));
                    return ret;
                case SPECIAL:
                    ret.setContent(revisionEntity.getMainPackage());
                    ret.setFilename(createFileName(revisionEntity.getProject(), true));
                    return ret;
                default:
                    throw new IllegalArgumentException("unknown application type : " + request.getApplicationType());
            }
        }
        return null;
    }

    @Transactional
    @AuthenticationRequired
    @Override
    public Revision getRevision(final RevisionRequest request) {
        final RevisionEntity revisionEntity = revisionDAO.findById(request.getRevisionId());
        final Revision revision = MAPPER.map(revisionEntity, Revision.class);
        createUrls(revision, revisionEntity);
        return revision;
    }


    private Revision updateRevisionPackages(RevisionEntity revisionEntity, final byte[] mainPackage, final byte[] specialPackage) {
        addPackages(revisionEntity, mainPackage, specialPackage);
        revisionEntity = revisionDAO.persist(revisionEntity);
        final Revision outcomeRevision = MAPPER.map(revisionEntity, Revision.class);
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    private void addPackages(final RevisionEntity revisionEntity, final byte[] mainPackage, final byte[] specialPackage) {
        if (mainPackage != null) {
            revisionEntity.setMainPackage(mainPackage);
            revisionEntity.setMainPackageLoaded(true);
        }
        if (specialPackage != null) {
            revisionEntity.setSpecialPackage(specialPackage);
            revisionEntity.setSpecialPackageLoaded(true);
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
