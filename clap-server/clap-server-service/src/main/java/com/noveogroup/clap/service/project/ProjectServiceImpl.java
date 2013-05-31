package com.noveogroup.clap.service.project;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.service.url.UrlService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class ProjectServiceImpl implements ProjectService {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @Inject
    private UrlService urlService;

    @Override
    public Project createProject(final Project project) {
        if (project.getCreationDate() == null) {
            project.setCreationDate(new Date());
        }
        final ProjectEntity projectEntity = MAPPER.map(project, ProjectEntity.class);
        return MAPPER.map(projectDAO.persist(projectEntity), Project.class);
    }

    @Override
    public Project getCreateUpdateProject(Project project) {
        ProjectEntity projectEntity = null;
        try {
            projectEntity = projectDAO.findProjectByExternalId(project.getExternalId());
        } catch (Exception e){
            projectEntity = MAPPER.map(project,ProjectEntity.class);
        }
        //TODO check null fields
        projectEntity = projectDAO.persist(projectEntity);
        return MAPPER.map(projectEntity,Project.class);
    }

    @Override
    public Project save(final Project project) {
        final ProjectEntity projectEntity = MAPPER.map(project, ProjectEntity.class);
        return MAPPER.map(projectDAO.persist(projectEntity), Project.class);
    }

    @Override
    public Project findById(final Long id) {
        final ProjectEntity projectEntity = projectDAO.findById(id);
        final Project project = MAPPER.map(projectEntity, Project.class);
        final List<Revision> revisions = project.getRevisions();
        for (int i=0; i<revisions.size();i++) {
            final Revision revision = revisions.get(i);
            final RevisionEntity revisionEntityOrigin = projectEntity.getRevisions().get(i);
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

    @Override
    public List<Project> findAllProjects() {
        final List<ProjectEntity> projectEntityList = projectDAO.selectAll();
        final List<Project> projectList = new ArrayList<Project>();
        for (final ProjectEntity projectEntity : projectEntityList) {
            projectList.add(MAPPER.map(projectEntity, Project.class));
        }
        return projectList;
    }


}
