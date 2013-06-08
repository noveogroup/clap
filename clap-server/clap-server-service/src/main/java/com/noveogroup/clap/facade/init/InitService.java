package com.noveogroup.clap.facade.init;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.dao.UserDAO;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import java.util.Date;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors(ClapMainInterceptor.class)
public class InitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @EJB
    private UserDAO userDAO;

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @Transactional
    public void initDB(){
        UserEntity user = new UserEntity();
        user.setLogin("test");
        user.setPassword("123");
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
