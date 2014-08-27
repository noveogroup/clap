package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.entity.message.ScreenshotMessageEntity;
import com.noveogroup.clap.entity.message.ScreenshotMessageEntityListenerDelegate;
import com.noveogroup.clap.service.file.FileService;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class ScreenshotMessageEntityListenerDelegateImpl implements ScreenshotMessageEntityListenerDelegate {

    @Inject
    private FileService fileService;

    @Override
    public void postRemove(final ScreenshotMessageEntity entity) {
        fileService.removeFile(entity.getScreenshotFileUrl());
    }

}
