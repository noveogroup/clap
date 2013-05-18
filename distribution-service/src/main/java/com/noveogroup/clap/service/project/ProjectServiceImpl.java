package com.noveogroup.clap.service.project;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.dao.ProjectDAO;
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
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({TransactionInterceptor.class})
public class ProjectServiceImpl implements ProjectService {

    private static Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @Inject
    private UrlService urlService;

    @Transactional
    @Override
    public ProjectDTO createProject(final ProjectDTO project) {
        if (project.getCreationDate() == null) {
            project.setCreationDate(new Date());
        }
        ProjectEntity projectEntity = MAPPER.map(project, ProjectEntity.class);
        return MAPPER.map(projectDAO.persist(projectEntity), ProjectDTO.class);
    }

    @Transactional
    @Override
    public ProjectDTO save(final ProjectDTO project) {
        ProjectEntity projectEntity = MAPPER.map(project, ProjectEntity.class);
        return MAPPER.map(projectDAO.persist(projectEntity), ProjectDTO.class);
    }

    @Transactional
    @Override
    public ProjectDTO findById(final Long id) {
        ProjectEntity projectEntity = projectDAO.findById(id);
        final ProjectDTO projectDTO = MAPPER.map(projectEntity, ProjectDTO.class);
        final List<RevisionDTO> revisions = projectDTO.getRevisions();
        for (int i=0; i<revisions.size();i++) {
            RevisionDTO revision = revisions.get(i);
            RevisionEntity revisionEntityOrigin = projectEntity.getRevisions().get(i);
            revision.setProjectId(projectDTO.getId());
            if(revisionEntityOrigin.isMainPackageLoaded()){
                revision.setMainPackageUrl(urlService.createUrl(revision.getId(),true));
            }
            if(revisionEntityOrigin.isSpecialPackageLoaded()) {
                revision.setSpecialPackageUrl(urlService.createUrl(revision.getId(),false));
            }
        }
        return projectDTO;
    }

    @Transactional
    @Override
    public List<ProjectDTO> findAllProjects() {
        List<ProjectEntity> projectEntityList = projectDAO.selectAll();
        List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectDTOList.add(MAPPER.map(projectEntity, ProjectDTO.class));
        }
        return projectDTOList;
    }


}
