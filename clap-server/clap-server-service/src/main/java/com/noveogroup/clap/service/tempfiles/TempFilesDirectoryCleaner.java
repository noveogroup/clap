package com.noveogroup.clap.service.tempfiles;

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
import java.io.File;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@Singleton
@Startup
public class TempFilesDirectoryCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempFilesDirectoryCleaner.class);

    @Inject
    private ConfigBean configBean;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void createTimer() {
        LOGGER.debug("Creating TempFilesDirectoryCleaner timer");
        timerService.createIntervalTimer(0, configBean.getTempFilesCleanInterval(), new TimerConfig(null, false));
    }

    @Timeout
    public void timeout(final Timer timer) {
        LOGGER.trace("deleting temp files");
        final List<String> tempFilesDirs = configBean.getTempFilesDirs();
        for (String tempFilesDirPath : tempFilesDirs) {
            final File tempFilesDir = new File(tempFilesDirPath);
            for (final File tempFile : tempFilesDir.listFiles()) {
                delete(tempFile);
            }
        }
    }

    private void delete(final File file) {
        if (file.isDirectory()) {
            for (final File innerFile : file.listFiles()) {
                delete(innerFile);
            }
        }
        final String name = file.getName();
        if (file.delete()) {
            LOGGER.trace(name + " deleted");
        } else {
            LOGGER.trace(name + " not deleted");
        }
    }
}
