package com.noveogroup.clap.converter;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.Project;
import com.noveogroup.clap.model.project.ImagedProject;
import com.noveogroup.clap.model.revision.Revision;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class ProjectConverter extends BaseConverter<Project, ProjectEntity> {

    private RevisionConverter revisionConverter = new RevisionConverter();

    public Project map(final ProjectEntity projectEntity) {
        return map(projectEntity, true);
    }


    public Project map(final ProjectEntity projectEntity, final boolean mapRevisions) {
        final Project ret = new Project();
        map(projectEntity, mapRevisions, ret);
        return ret;
    }

    private void map(final ProjectEntity projectEntity, final boolean mapRevisions, final Project ret) {
        map(ret, projectEntity);
        ret.setCreationDate(projectEntity.getCreationDate());
        ret.setDescription(projectEntity.getDescription());
        ret.setExternalId(projectEntity.getExternalId());
        ret.setName(projectEntity.getName());
        List<RevisionEntity> revisionEntities = projectEntity.getRevisions();
        if (mapRevisions && CollectionUtils.isNotEmpty(revisionEntities)) {
            final List<Revision> revisions = Lists.newArrayList();
            for (RevisionEntity revisionEntity : revisionEntities) {
                revisions.add(revisionConverter.map(revisionEntity));
            }
            ret.setRevisions(revisions);
        }
    }

    public ImagedProject mapToImagedProject(final ProjectEntity projectEntity,
                                            final ConfigBean configBean) {
        return mapToImagedProject(projectEntity, configBean, true);
    }

    public ImagedProject mapToImagedProject(final ProjectEntity projectEntity,
                                            final ConfigBean configBean,
                                            final boolean mapRevisions) {
        final ImagedProject ret = new ImagedProject();
        map(projectEntity, mapRevisions, ret);
        if(StringUtils.isNotBlank(projectEntity.getIconFilePath())){
            ret.setIconFileUrl(configBean.getDownloadProjectIconUrl(ret.getId()));
        }
        return ret;
    }

    public ProjectEntity map(final Project project) {
        return map(project, true);
    }

    public ProjectEntity map(final Project project, final boolean mapRevisions) {
        final ProjectEntity ret = new ProjectEntity();
        map(ret, project);
        ret.setCreationDate(project.getCreationDate());
        ret.setDescription(project.getDescription());
        ret.setExternalId(project.getExternalId());
        ret.setName(project.getName());

        List<Revision> revisions = project.getRevisions();
        if (mapRevisions && CollectionUtils.isNotEmpty(revisions)) {
            final List<RevisionEntity> revisionEntities = Lists.newArrayList();
            for (Revision revision : revisions) {
                revisionEntities.add(revisionConverter.map(revision));
            }
            ret.setRevisions(revisionEntities);
        }

        return ret;
    }

    public void setRevisionConverter(final RevisionConverter revisionConverter) {
        this.revisionConverter = revisionConverter;
    }

    public void updateEntity(final Project project, final ProjectEntity toUpdate) {
        toUpdate.setName(project.getName());
        toUpdate.setDescription(project.getDescription());
    }
}
