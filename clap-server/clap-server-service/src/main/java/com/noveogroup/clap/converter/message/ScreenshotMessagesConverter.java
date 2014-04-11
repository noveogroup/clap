package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.entity.message.ScreenshotMessageEntity;
import com.noveogroup.clap.model.message.ScreenshotMessage;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessagesConverter implements OneTypeMessagesConverter<ScreenshotMessage,ScreenshotMessageEntity> {
    @Override
    public ScreenshotMessage map(final ScreenshotMessageEntity messageEntity) {
        final ScreenshotMessage screenshotMessage = new ScreenshotMessage();

        return screenshotMessage;
    }

    @Override
    public ScreenshotMessageEntity map(final ScreenshotMessage message) {
        return null;
    }

    @Override
    public Class<ScreenshotMessage> getMessageClass() {
        return ScreenshotMessage.class;
    }

    @Override
    public Class<ScreenshotMessageEntity> getMessageEntityClass() {
        return ScreenshotMessageEntity.class;
    }
}
