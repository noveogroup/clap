package com.noveogroup.clap.entity.message;

import com.noveogroup.clap.entity.BaseEntityListener;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessageEntityListener extends BaseEntityListener<ScreenshotMessageEntityListenerDelegate,
        ScreenshotMessageEntity> {


    @Override
    protected Class<ScreenshotMessageEntityListenerDelegate> getDelegateClass() {
        return ScreenshotMessageEntityListenerDelegate.class;
    }
}
