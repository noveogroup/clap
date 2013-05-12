package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.entity.revision.Revision;
import com.noveogroup.clap.entity.revision.RevisionType;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.service.url.UrlService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Revision revision = MAPPER.map(revisionDTO, Revision.class);
        if (revision.getTimestamp() == null) {
            revision.setTimestamp(new Date().getTime());
        }
        if (revision.getRevisionType() == null) {
            revision.setRevisionType(RevisionType.DEVELOP);
        }
        addPackages(revision,mainPackage,specialPackage);
        Project project = projectDAO.findById(projectId);
        revision.setProject(project);
        project.getRevisions().add(revision);

        projectDAO.persist(project);
        revision = revisionDAO.persist(revision);
        RevisionDTO outcomeRevision = MAPPER.map(revision, RevisionDTO.class);
        outcomeRevision.setProjectId(projectId);
        createUrls(outcomeRevision,revision);
        return outcomeRevision;
    }

    @Transactional
    @Override
    public RevisionDTO updateRevisionPackages(RevisionDTO revisionDTO, byte[] mainPackage, byte[] specialPackage) {
        Revision revision = revisionDAO.findById(revisionDTO.getId());
        return updateRevisionPackages(revision,mainPackage,specialPackage);
    }

    @Transactional
    @Override
    public RevisionDTO updateRevisionPackages(Long revisionTimestamp, byte[] mainPackage, byte[] specialPackage) {
       Revision revision = revisionDAO.getRevisionByTimestamp(revisionTimestamp);
       return updateRevisionPackages(revision,mainPackage,specialPackage);
    }

    @Transactional
    @Override
    public ApplicationFile getApplication(final Long revisionId, final Integer type) {
        Revision revision = revisionDAO.findById(revisionId);
        if (revision != null) {
            ApplicationFile ret = new ApplicationFile();
            if (type == 1) {
                ret.setContent(revision.getSpecialPackage());
                ret.setFilename(createFileName(revision.getProject(),false));
                return ret;
            } else if (type == 0) {
                ret.setContent(revision.getMainPackage());
                ret.setFilename(createFileName(revision.getProject(), true));
                return ret;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public RevisionDTO getRevision(Long timestamp) {
        Revision revisionByTimestamp = revisionDAO.getRevisionByTimestamp(timestamp);
        RevisionDTO revisionDTO = MAPPER.map(revisionByTimestamp, RevisionDTO.class);
        createUrls(revisionDTO,revisionByTimestamp);
        return revisionDTO;
    }

    @Override
    public RevisionDTO findById(Long id) {
        Revision revision = revisionDAO.findById(id);
        return MAPPER.map(revision,RevisionDTO.class);
    }

    private RevisionDTO updateRevisionPackages(Revision revision, byte[] mainPackage, byte[] specialPackage){
        addPackages(revision,mainPackage,specialPackage);
        revision = revisionDAO.persist(revision);
        RevisionDTO outcomeRevision = MAPPER.map(revision, RevisionDTO.class);
        createUrls(outcomeRevision,revision);
        return outcomeRevision;
    }

    private void addPackages(Revision revision,byte[] mainPackage, byte[] specialPackage){
        if(mainPackage != null){
            revision.setMainPackage(mainPackage);
            revision.setMainPackageLoaded(true);
        }
        if (specialPackage != null) {
            revision.setSpecialPackage(specialPackage);
            revision.setSpecialPackageLoaded(true);
        }
    }

    private void createUrls(RevisionDTO outcomeRevision, Revision revision){
        if(revision.isMainPackageLoaded()){
            outcomeRevision.setMainPackageUrl(urlService.createUrl(outcomeRevision.getId(),true));
        }
        if(revision.isSpecialPackageLoaded()){
            outcomeRevision.setSpecialPackageUrl(urlService.createUrl(outcomeRevision.getId(),false));
        }
    }

    private String createFileName(Project project,boolean mainPackage){
        return project.getName() + (mainPackage ? "" : "_hacked")+ ".apk" ;
    }

}
