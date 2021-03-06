package com.noveogroup.clap.service.revision;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.RevisionConverter;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.RevisionVariantDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.model.request.revision.CreateRevisionVariantRequest;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.service.file.FileService;
import com.noveogroup.clap.service.user.UserService;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
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
    private RevisionVariantDAO revisionVariantDAO;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private ConfigBean configBean;

    @Mock
    private RevisionConverter revisionConverter;

    @Mock
    private UserService userService;

    @Mock
    private FileService fileService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void testAddOrGetRevision() throws Exception {

        final Date now = new Date();
        final CreateRevisionVariantRequest request = new CreateRevisionVariantRequest();
        final String mockProjectExternalId = "mockProjectExternalId";
        request.setProjectExternalId(mockProjectExternalId);
        final Revision revision = new Revision();
        final String mockHash = "mockHash";
        revision.setHash(mockHash);
        request.setRevisionHash(mockHash);
        final String mockVariantHash = "mockVariantHash";
        request.setVariantHash(mockVariantHash);

        final RevisionEntity mapped = new RevisionEntity();
        when(revisionConverter.map(revision)).thenReturn(mapped);
        when(revisionVariantDAO.getRevisionByHash(mockVariantHash)).thenReturn(null);
        when(revisionDAO.getRevisionByHashOrNull(mockHash)).thenReturn(null);
        final ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(123L);
        projectEntity.setExternalId(mockProjectExternalId);
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
        re3.setVariants(new ArrayList<RevisionVariantEntity>());
        RevisionVariantEntity variantEntity = new RevisionVariantEntity();
        re3.getVariants().add(variantEntity);
        variantEntity.setPackageFileUrl("mockFileUrl1");
        variantEntity = new RevisionVariantEntity();
        variantEntity.setPackageFileUrl("mockFileUrl2");
        re3.getVariants().add(variantEntity);
        revisionEntities.add(re3);
        final RevisionEntity re4 = new RevisionEntity();
        re4.setId(4L);
        re4.setTimestamp(now.getTime() - 2000);
        revisionEntities.add(re4);

        when(revisionDAO.findForProjectAndType(123L, RevisionType.DEVELOP)).thenReturn(revisionEntities);
        final boolean created = revisionService.createRevisionVariant(request);
        verify(fileService).removeFile("mockFileUrl1");
        verify(fileService).removeFile("mockFileUrl2");
        verify(revisionDAO).remove(re3);
        verify(revisionDAO).persist(argThat(new RevisionEntityMatcher(mockHash)));
        assertTrue(created);
        when(revisionVariantDAO.getRevisionByHash(mockVariantHash)).thenReturn(new RevisionVariantEntity());

        final boolean created2 = revisionService.createRevisionVariant(request);
        assertFalse(created2);
    }

    private static class RevisionEntityMatcher extends BaseMatcher<RevisionEntity>{


        private final String hash;

        private RevisionEntityMatcher(final String hash) {
            this.hash = hash;
        }

        @Override
        public boolean matches(final Object o) {
            RevisionEntity entity = (RevisionEntity) o;
            return entity.getHash().equals(hash);
        }

        @Override
        public void describeTo(final Description description) {

        }
    }
}
