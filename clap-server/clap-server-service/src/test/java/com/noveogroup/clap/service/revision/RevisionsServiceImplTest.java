package com.noveogroup.clap.service.revision;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.RevisionConverter;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.StreamedPackage;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Andrey Sokolov
 */
public class RevisionsServiceImplTest {

    @InjectMocks
    private RevisionServiceImpl revisionService;

    @Mock
    private RevisionDAO revisionDAO;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private ConfigBean configBean;

    @Mock
    private RevisionConverter revisionConverter;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void testAddOrGetRevision() throws Exception {

        final Date now = new Date();
        final AddOrGetRevisionRequest request = new AddOrGetRevisionRequest();
        final String mockProjectExternalId = "mockProjectExternalId";
        request.setProjectExternalId(mockProjectExternalId);
        final Revision revision = new Revision();
        final String mockHash = "mockHash";
        revision.setHash(mockHash);
        request.setRevision(revision);

        final RevisionEntity mapped = new RevisionEntity();
        when(revisionConverter.map(revision)).thenReturn(mapped);
        when(revisionDAO.getRevisionByHashOrNull(mockHash)).thenReturn(null);
        final ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(123L);
        when(projectDAO.findProjectByExternalIdOrReturnNull(mockProjectExternalId)).thenReturn(projectEntity);
        List<RevisionEntity> revisionEntities = Lists.newArrayList();
        final RevisionEntity re1 = new RevisionEntity();
        re1.setId(1L);
        re1.setTimestamp(now.getTime() - 5000);
        revisionEntities.add(re1);
        final RevisionEntity re2 = new RevisionEntity();
        re2.setId(2L);
        re2.setTimestamp(now.getTime() - 3000);
        revisionEntities.add(re2);
        final RevisionEntity re3 = new RevisionEntity();
        re3.setId(3L);
        re3.setTimestamp(now.getTime() - 7000);
        revisionEntities.add(re3);
        final RevisionEntity re4 = new RevisionEntity();
        re4.setId(4L);
        re4.setTimestamp(now.getTime() - 2000);
        revisionEntities.add(re4);

        when(revisionDAO.findForProjectAndType(123L, RevisionType.DEVELOP)).thenReturn(revisionEntities);
        final RevisionEntity persisted = new RevisionEntity();
        when(revisionDAO.persist(eq(mapped),any(StreamedPackage.class),any(StreamedPackage.class))).thenReturn(persisted);
        final Revision mappedFromPersisted = new Revision();
        when(revisionConverter.map(persisted)).thenReturn(mappedFromPersisted);
        final Revision revision1 = revisionService.addOrGetRevision(request);
        verify(revisionDAO).remove(re3);
        assertEquals(revision1, mappedFromPersisted);
    }
}
