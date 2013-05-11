package com.noveogroup.clap.service.revision.impl;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.entity.revision.Revision;
import com.noveogroup.clap.entity.revision.RevisionType;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.service.revision.RevisionService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * @author Mikhail Demidov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({TransactionInterceptor.class})
public class RevisionServiceImpl implements RevisionService {

    private static Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @Inject
    private ConfigBean configBean;

    @Override
    public RevisionDTO addRevision(final Long projectId, final RevisionDTO revisionDTO, final byte[] mainPackage, final byte[] specialPackage) {
        Revision revision = MAPPER.map(revisionDTO, Revision.class);
        if (revision.getTimestamp() == null) {
            revision.setTimestamp(System.currentTimeMillis());
        }
        if (revision.getRevisionType() == null) {
            revision.setRevisionType(RevisionType.DEVELOP);
        }
        if (mainPackage != null) {
            revision.setMainPackage(mainPackage);
        }
        if (specialPackage != null) {
            revision.setSpecialPackage(specialPackage);
        }
        Project project = projectDAO.findById(projectId);
        revision.setProject(project);
        project.getRevisions().add(revision);

        projectDAO.persist(project);
        revision = revisionDAO.persist(revision);
        RevisionDTO outcomeRevision = MAPPER.map(revision, RevisionDTO.class);
        outcomeRevision.setProjectId(projectId);
//        String baseUrl = configBean.getDownloadApkUrl();
        outcomeRevision.setMainPackageUrl("bla" + "/" + projectId + "/0");
        outcomeRevision.setSpecialPackageUrl("bla" + "/" + projectId + "/1");
        return outcomeRevision;
    }

    @Override
    public byte[] getApplication(final Long revisionId, final Integer type) {
        Revision revision = revisionDAO.findById(revisionId);
        if (revision != null) {
            if (type == 1) {
                return revision.getSpecialPackage();
            } else if (type == 0) {
                return revision.getMainPackage();
            }
        }
        return null;
    }


}
