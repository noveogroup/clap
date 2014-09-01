package com.noveogroup.clap.service.cleanup;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.model.revision.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.ExcludeDefaultInterceptors;
import java.util.Date;

/**
 * @author Andrey Sokolov
 */
@Singleton
@Startup
@ExcludeDefaultInterceptors
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CleanUpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanUpService.class);
    @EJB
    private MessageDAO messageDAO;

    @Resource
    private TimerService timerService;

    @Inject
    private ConfigBean configBean;

    @PostConstruct
    public void createTimer() {
        LOGGER.debug("Creating CleanUpService timer");
        timerService.createIntervalTimer(configBean.getMessagesCleanupInterval(),
                configBean.getMessagesCleanupInterval(),
                new TimerConfig(null, false));
    }

    @Timeout
    public void timeout(final Timer timer) {
        LOGGER.trace("deleting messages");
        try {
            cleanUpMessages();
        } catch (Throwable e) {
            LOGGER.error("cleaning messages error", e);
        }
    }

    public void cleanUpMessages() {
        final long now = new Date().getTime();
        for (RevisionType type : RevisionType.values()) {
            removeMessages(now, type);
        }
    }

    private void removeMessages(final long now, final RevisionType type) {
        final Long liveTime = configBean.getMessagesLiveTime().get(type);
        messageDAO.deleteMessages(now - liveTime, type);
    }
}
