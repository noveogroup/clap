package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.ScreenshotMessageEntity;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.Date;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessagesConverter extends BaseMessagesConverter
        implements OneTypeMessagesConverter<ScreenshotMessage, ScreenshotMessageEntity> {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Override
    public ScreenshotMessage map(final ScreenshotMessageEntity messageEntity,final ConfigBean configBean) {
        final ScreenshotMessage screenshotMessage = new ScreenshotMessage();
        final Long id = messageEntity.getId();
        map(messageEntity,screenshotMessage);
        screenshotMessage.setScreenshotUrl(configBean.getDownloadScreenshotUrl(id));
        final Date timestamp = messageEntity.getTimestamp();
        if(timestamp != null){
            screenshotMessage.setTimestamp(timestamp.getTime());
        }
        return screenshotMessage;
    }

    @Override
    public ScreenshotMessageEntity map(final ScreenshotMessage message) {
        final ScreenshotMessageEntity screenshotMessageEntity = MAPPER.map(message,ScreenshotMessageEntity.class);
        screenshotMessageEntity.setScreenshotFileUrl(message.getScreenshotUrl());
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
