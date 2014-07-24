package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.ScreenshotMessageEntity;
import com.noveogroup.clap.entity.user.UserEntity;
import com.noveogroup.clap.model.message.ScreenshotMessage;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessagesConverter
        implements OneTypeMessagesConverter<ScreenshotMessage, ScreenshotMessageEntity> {
    @Override
    public ScreenshotMessage map(final ScreenshotMessageEntity messageEntity,final ConfigBean configBean) {
        final ScreenshotMessage screenshotMessage = new ScreenshotMessage();
        final Long id = messageEntity.getId();
        screenshotMessage.setId(id);
        screenshotMessage.setScreenshotUrl(configBean.getDownloadScreenshotUrl(id));
        screenshotMessage.setTimestamp(messageEntity.getTimestamp());
        final UserEntity uploadedBy = messageEntity.getUploadedBy();
        if(uploadedBy != null){
            screenshotMessage.setUploadedBy(uploadedBy.getLogin());
        }
        return screenshotMessage;
    }

    @Override
    public ScreenshotMessageEntity map(final ScreenshotMessage message) {
        final ScreenshotMessageEntity screenshotMessageEntity = new ScreenshotMessageEntity();
        screenshotMessageEntity.setScreenshotFileUrl(message.getScreenshotUrl());
        screenshotMessageEntity.setTimestamp(message.getTimestamp());
        return screenshotMessageEntity;
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
