package com.noveogroup.clap.service.init;

import com.noveogroup.clap.auth.PasswordsHashCalculator;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.user.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.Date;

/**
 * @author Andrey Sokolov
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@Startup
public class InitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @EJB
    private UserDAO userDAO;

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @PostConstruct
    public void initDB(){
        UserEntity user = new UserEntity();
        user.setLogin("test");
        user.setAuthenticationKey(PasswordsHashCalculator.calculatePasswordHash("123"));
        user.setRole(Role.ADMIN);
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
        revision = revisionDAO.persist(revision);
        LOGGER.debug("revision created : " + revision);
    }

}
