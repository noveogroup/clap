package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionType;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.RevisionDTO;
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
@Interceptors({TransactionInterceptor.class})
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
    public RevisionDTO addRevision(final Long projectId, final RevisionDTO revisionDTO, final byte[] mainPackage, final byte[] specialPackage) {
        RevisionEntity revisionEntity = MAPPER.map(revisionDTO, RevisionEntity.class);
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
        RevisionDTO outcomeRevision = MAPPER.map(revisionEntity, RevisionDTO.class);
        outcomeRevision.setProjectId(projectId);
        createUrls(outcomeRevision, revisionEntity);
        return outcomeRevision;
    }

    @Transactional
    @Override
    public RevisionDTO updateRevisionPackages(RevisionDTO revisionDTO, byte[] mainPackage, byte[] specialPackage) {
        RevisionEntity revisionEntity = revisionDAO.findById(revisionDTO.getId());
        return updateRevisionPackages(revisionEntity,mainPackage,specialPackage);
    }

    @Transactional
    @Override
    public RevisionDTO updateRevisionPackages(Long revisionTimestamp, byte[] mainPackage, byte[] specialPackage) {
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
    public RevisionDTO getRevision(Long timestamp) {
        RevisionEntity revisionEntityByTimestamp = revisionDAO.getRevisionByTimestamp(timestamp);
        RevisionDTO revisionDTO = MAPPER.map(revisionEntityByTimestamp, RevisionDTO.class);
        createUrls(revisionDTO, revisionEntityByTimestamp);
        return revisionDTO;
    }

    @Override
    public RevisionDTO findById(Long id) {
        RevisionEntity revisionEntity = revisionDAO.findById(id);
        RevisionDTO revisionDTO = MAPPER.map(revisionEntity, RevisionDTO.class);
        createUrls(revisionDTO, revisionEntity);
        return revisionDTO;
    }

    private RevisionDTO updateRevisionPackages(RevisionEntity revisionEntity, byte[] mainPackage, byte[] specialPackage){
        addPackages(revisionEntity,mainPackage,specialPackage);
        revisionEntity = revisionDAO.persist(revisionEntity);
        RevisionDTO outcomeRevision = MAPPER.map(revisionEntity, RevisionDTO.class);
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

    private void createUrls(RevisionDTO outcomeRevision, RevisionEntity revisionEntity){
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
