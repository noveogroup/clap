package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionType;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.service.url.UrlService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.Date;

/**
 * @author Mikhail Demidov
 */
@Stateless
@Interceptors({ClapMainInterceptor.class})
@TransactionManagement(TransactionManagementType.BEAN)
public class RevisionServiceImpl implements RevisionService {

    private static Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @Inject
    private UrlService urlService;

    @Transactional
    @Override
    public Revision addRevision(final Long projectId, final Revision revision, final byte[] mainPackage, final byte[] specialPackage) {
        RevisionEntity revisionEntity = MAPPER.map(revision, RevisionEntity.class);
        if (revisionEntity.getTimestamp() == null) {
            revisionEntity.setTimestamp(new Date().getTime());
        }
        if (revisionEntity.getRevisionType() == null) {
            revisionEntity.setRevisionType(RevisionType.DEVELOP);
        }
        addPackages(revisionEntity,mainPackage,specialPackage);
        ProjectEntity projectEntity = projectDAO.findById(projectId);
        revisionEntity.setProject(projectEntity);
        projectEntity.getRevisions().add(revisionEntity);

        projectDAO.persist(projectEntity);
        revisionEntity = revisionDAO.persist(revisionEntity);
        Revision outcomeRevision = MAPPER.map(revisionEntity, Revision.class);
        outcomeRevision.setProjectId(projectId);
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    @Transactional
    @Override
    public Revision updateRevisionPackages(Revision revision, byte[] mainPackage, byte[] specialPackage) {
        RevisionEntity revisionEntity = revisionDAO.findById(revision.getId());
        return updateRevisionPackages(revisionEntity,mainPackage,specialPackage);
    }

    @Transactional
    @Override
    public Revision updateRevisionPackages(Long revisionTimestamp, byte[] mainPackage, byte[] specialPackage) {
       RevisionEntity revisionEntity = revisionDAO.getRevisionByTimestamp(revisionTimestamp);
       return updateRevisionPackages(revisionEntity,mainPackage,specialPackage);
    }

    @Transactional
    @Override
    public ApplicationFile getApplication(final Long revisionId, final Integer type) {
        RevisionEntity revisionEntity = revisionDAO.findById(revisionId);
        if (revisionEntity != null) {
            ApplicationFile ret = new ApplicationFile();
            if (type == 1) {
                ret.setContent(revisionEntity.getSpecialPackage());
                ret.setFilename(createFileName(revisionEntity.getProject(),false));
                return ret;
            } else if (type == 0) {
                ret.setContent(revisionEntity.getMainPackage());
                ret.setFilename(createFileName(revisionEntity.getProject(), true));
                return ret;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public Revision getRevision(Long timestamp) {
        RevisionEntity revisionEntityByTimestamp = revisionDAO.getRevisionByTimestamp(timestamp);
        Revision revision = MAPPER.map(revisionEntityByTimestamp, Revision.class);
        createUrls(revision, revisionEntityByTimestamp);
        return revision;
    }

    @Override
    public Revision findById(Long id) {
        RevisionEntity revisionEntity = revisionDAO.findById(id);
        Revision revision = MAPPER.map(revisionEntity, Revision.class);
        createUrls(revision, revisionEntity);
        return revision;
    }

    private Revision updateRevisionPackages(RevisionEntity revisionEntity, byte[] mainPackage, byte[] specialPackage){
        addPackages(revisionEntity,mainPackage,specialPackage);
        revisionEntity = revisionDAO.persist(revisionEntity);
        Revision outcomeRevision = MAPPER.map(revisionEntity, Revision.class);
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    private void addPackages(RevisionEntity revisionEntity,byte[] mainPackage, byte[] specialPackage){
        if(mainPackage != null){
            revisionEntity.setMainPackage(mainPackage);
            revisionEntity.setMainPackageLoaded(true);
        }
        if (specialPackage != null) {
            revisionEntity.setSpecialPackage(specialPackage);
            revisionEntity.setSpecialPackageLoaded(true);
        }
    }

    private void createUrls(Revision outcomeRevision, RevisionEntity revisionEntity){
        if(revisionEntity.isMainPackageLoaded()){
            outcomeRevision.setMainPackageUrl(urlService.createUrl(outcomeRevision.getId(),true));
        }
        if(revisionEntity.isSpecialPackageLoaded()){
            outcomeRevision.setSpecialPackageUrl(urlService.createUrl(outcomeRevision.getId(),false));
        }
    }

    private String createFileName(ProjectEntity projectEntity,boolean mainPackage){
        return projectEntity.getName() + (mainPackage ? "" : "_hacked")+ ".apk" ;
    }

}
