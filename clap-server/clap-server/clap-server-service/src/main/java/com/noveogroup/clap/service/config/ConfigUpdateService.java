package com.noveogroup.clap.service.config;

import com.noveogroup.clap.config.ConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.interceptor.ExcludeDefaultInterceptors;
import java.io.IOException;

/**
 * @author Andrey Sokolov
 */
@Singleton
@Startup
@ExcludeDefaultInterceptors
public class ConfigUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUpdateService.class);
    @Inject
    private ConfigBean configBean;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void createTimer() {
        LOGGER.debug("Creating TempFilesDirectoryCleaner timer");
        timerService.createIntervalTimer(0, configBean.getUpdateConfigInterval(), new TimerConfig(null, false));
    }

    @Timeout
    public void timeout(final Timer timer) {
        LOGGER.trace("deleting temp files");
        try {
            configBean.updateConfig();
        } catch (IOException e) {
            LOGGER.error("Exception while update config: ", e);
        }
    }
}
