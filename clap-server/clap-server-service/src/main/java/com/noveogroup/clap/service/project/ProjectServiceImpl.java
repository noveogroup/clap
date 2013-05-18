package com.noveogroup.clap.service.project;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.revision.Revision;
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
    public Project createProject(final Project project) {
        if (project.getCreationDate() == null) {
            project.setCreationDate(new Date());
        }
        ProjectEntity projectEntity = MAPPER.map(project, ProjectEntity.class);
        return MAPPER.map(projectDAO.persist(projectEntity), Project.class);
    }

    @Transactional
    @Override
    public Project save(final Project project) {
        ProjectEntity projectEntity = MAPPER.map(project, ProjectEntity.class);
        return MAPPER.map(projectDAO.persist(projectEntity), Project.class);
    }

    @Transactional
    @Override
    public Project findById(final Long id) {
        ProjectEntity projectEntity = projectDAO.findById(id);
        final Project project = MAPPER.map(projectEntity, Project.class);
        final List<Revision> revisions = project.getRevisions();
        for (int i=0; i<revisions.size();i++) {
            Revision revision = revisions.get(i);
            RevisionEntity revisionEntityOrigin = projectEntity.getRevisions().get(i);
            revision.setProjectId(project.getId());
            if(revisionEntityOrigin.isMainPackageLoaded()){
                revision.setMainPackageUrl(urlService.createUrl(revision.getId(),true));
            }
            if(revisionEntityOrigin.isSpecialPackageLoaded()) {
                revision.setSpecialPackageUrl(urlService.createUrl(revision.getId(),false));
            }
        }
        return project;
    }

    @Transactional
    @Override
    public List<Project> findAllProjects() {
        List<ProjectEntity> projectEntityList = projectDAO.selectAll();
        List<Project> projectList = new ArrayList<Project>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectList.add(MAPPER.map(projectEntity, Project.class));
        }
        return projectList;
    }


}
