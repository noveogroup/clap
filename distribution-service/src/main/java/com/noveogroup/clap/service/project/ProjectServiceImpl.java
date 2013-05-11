package com.noveogroup.clap.service.project;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.entity.revision.Revision;
import com.noveogroup.clap.entity.revision.RevisionType;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.model.ProjectDTO;
import com.noveogroup.clap.model.revision.RevisionDTO;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.service.url.UrlService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.hibernate.Hibernate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
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

    @Override
    public ProjectDTO createProject(final ProjectDTO project) {
        if (project.getCreationDate() == null) {
            project.setCreationDate(System.currentTimeMillis());
        }
        Project inProject = MAPPER.map(project, Project.class);
        return MAPPER.map(projectDAO.persist(inProject), ProjectDTO.class);
    }

    @Override
    public ProjectDTO save(final ProjectDTO project) {
        Project inProject = MAPPER.map(project, Project.class);
        return MAPPER.map(projectDAO.persist(inProject), ProjectDTO.class);
    }

    @Override
    public ProjectDTO findById(final Long id) {
        Project project = projectDAO.findById(id);
        final ProjectDTO projectDTO = MAPPER.map(project, ProjectDTO.class);
        final List<RevisionDTO> revisions = projectDTO.getRevisions();
        for (int i=0; i<revisions.size();i++) {
            RevisionDTO revision = revisions.get(i);
            Revision revisionOrigin = project.getRevisions().get(i);
            revision.setProjectId(projectDTO.getId());
            if(revisionOrigin.isMainPackageLoaded()){
                revision.setMainPackageUrl(urlService.createUrl(revision.getId(),true));
            }
            if(revisionOrigin.isSpecialPackageLoaded()) {
                revision.setSpecialPackageUrl(urlService.createUrl(revision.getId(),false));
            }
        }
        return projectDTO;
    }

    @Override
    public List<ProjectDTO> findAllProjects() {
        List<Project> projectList = projectDAO.selectAll();
        List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();
        for (Project project : projectList) {
            projectDTOList.add(MAPPER.map(project, ProjectDTO.class));
        }
        return projectDTOList;
    }


}
