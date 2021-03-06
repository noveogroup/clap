package com.noveogroup.clap.service.init;

import com.google.common.collect.Lists;
import com.noveogroup.clap.auth.PasswordsHashCalculator;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.RevisionVariantDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.project.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.user.ClapPermission;
import com.noveogroup.clap.model.user.Role;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.ExcludeDefaultInterceptors;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Andrey Sokolov
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@Startup
@ExcludeDefaultInterceptors
public class InitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @EJB
    private UserDAO userDAO;

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private RevisionVariantDAO revisionVariantDAO;

    @Inject
    private ConfigBean configBean;

    @PostConstruct
    public void initDB() {
        final Date tokenExpireDate = new Date();
        tokenExpireDate.setTime(tokenExpireDate.getTime()+configBean.getTokenExpirationTime());
        List<UserEntity> userEntities = userDAO.selectAll();
        if(CollectionUtils.isNotEmpty(userEntities)){
            return;
        }
        UserEntity user = new UserEntity();
        user.setLogin("unnamed");
        user.setHashedPassword(PasswordsHashCalculator.calculatePasswordHash("unnamed_password"));
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiration(tokenExpireDate);
        user.setRole(Role.DEVELOPER);
        user.setClapPermissions(Lists.newArrayList(ClapPermission.values()));
        user = userDAO.persist(user);
        LOGGER.debug("user created = " + user);


        user = new UserEntity();
        user.setLogin("asokolov");
        user.setHashedPassword(PasswordsHashCalculator.calculatePasswordHash("testtest"));
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiration(tokenExpireDate);
        user.setClapPermissions(Lists.newArrayList(ClapPermission.values()));
        user.setRole(Role.ADMIN);
        user = userDAO.persist(user);
        LOGGER.debug("user created = " + user);


        user = new UserEntity();
        user.setLogin("pstepanov");
        user.setHashedPassword(PasswordsHashCalculator.calculatePasswordHash("pstepanov"));
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiration(tokenExpireDate);
        user.setRole(Role.DEVELOPER);
        user.setClapPermissions(Lists.newArrayList(ClapPermission.values()));
        user = userDAO.persist(user);
        LOGGER.debug("user created = " + user);

        ProjectEntity project = new ProjectEntity();
        project.setName("test_project");
        project.setExternalId("test_ext_id");
        project.setCreationDate(new Date(1368308474));
        project = projectDAO.persist(project);
        LOGGER.debug("project created : " + project);
        RevisionEntity revision = new RevisionEntity();
        revision.setTimestamp(1368318776L);
        revision.setHash("test_hash");
        revision.setProject(project);
        revision.setRevisionType(RevisionType.DEVELOP);
        revision.setVariants(new ArrayList<RevisionVariantEntity>());
        revision = revisionDAO.persist(revision);
        final RevisionVariantEntity revisionVariantEntity = new RevisionVariantEntity();
        revisionVariantEntity.setFullHash("testFullHash");
        revisionVariantEntity.setPackageVariant("testPackageVariant");
        revisionVariantEntity.setPackageFileUrl("testPackageUrl");
        revisionVariantEntity.setRandom("testRandom");
        revisionVariantEntity.setRevision(revision);
        revision.getVariants().add(revisionVariantEntity);
        revisionVariantDAO.persist(revisionVariantEntity);
        LOGGER.debug("revision created : " + revision);
    }

}
