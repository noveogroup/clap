package com.noveogroup.clap.converter;

import com.google.common.collect.Maps;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.converter.message.CrashMessagesConverter;
import com.noveogroup.clap.converter.message.InfoMessageConverter;
import com.noveogroup.clap.converter.message.LogsBunchMessageConverter;
import com.noveogroup.clap.converter.message.OneTypeMessagesConverter;
import com.noveogroup.clap.converter.message.ScreenshotMessagesConverter;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.model.message.BaseMessage;

import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public class MessagesConverter extends BaseConverter {

    private final Map<Class, OneTypeMessagesConverter> converterMapByDTO = Maps.newHashMap();
    private final Map<Class, OneTypeMessagesConverter> converterMapByEntity = Maps.newHashMap();

    public MessagesConverter() {
        final CrashMessagesConverter crashMessagesConverter = new CrashMessagesConverter();
        final ScreenshotMessagesConverter screenshotMessagesConverter = new ScreenshotMessagesConverter();
        final LogsBunchMessageConverter logsBunchMessageConverter = new LogsBunchMessageConverter();
        final InfoMessageConverter infoMessageConverter = new InfoMessageConverter();
        converterMapByDTO.put(crashMessagesConverter.getMessageClass(), crashMessagesConverter);
        converterMapByDTO.put(screenshotMessagesConverter.getMessageClass(), screenshotMessagesConverter);
        converterMapByDTO.put(logsBunchMessageConverter.getMessageClass(), logsBunchMessageConverter);
        converterMapByDTO.put(infoMessageConverter.getMessageClass(), infoMessageConverter);

        converterMapByEntity.put(crashMessagesConverter.getMessageEntityClass(), crashMessagesConverter);
        converterMapByEntity.put(screenshotMessagesConverter.getMessageEntityClass(), screenshotMessagesConverter);
        converterMapByEntity.put(logsBunchMessageConverter.getMessageEntityClass(), logsBunchMessageConverter);
        converterMapByEntity.put(infoMessageConverter.getMessageEntityClass(), infoMessageConverter);
    }

    public BaseMessage map(final BaseMessageEntity entity, final ConfigBean configBean) {
        final Class<? extends BaseMessageEntity> entityClass = entity.getEntityType();
        final OneTypeMessagesConverter messagesConverter = converterMapByEntity.get(entityClass);
        if (messagesConverter != null) {
            BaseMessage message = messagesConverter.map(entity, configBean);
            return message;
        } else {
            throw new IllegalStateException("no converter found for " + entityClass);
        }
    }

    public BaseMessageEntity map(final BaseMessage message) {
        final Class<? extends BaseMessage> messageClass = message.getClass();
        OneTypeMessagesConverter messagesConverter = converterMapByDTO.get(messageClass);
        if(messagesConverter == null){
            for(Class messageType: converterMapByDTO.keySet()){
                if(messageType.isAssignableFrom(message.getClass())){
                    messagesConverter = converterMapByDTO.get(messageType);
                    break;
                }
            }
        }
        if (messagesConverter != null) {
            BaseMessageEntity messageEntity = messagesConverter.map(message);
            return messageEntity;
        } else {
            throw new IllegalStateException("no converter found for " + messageClass);
        }
    }
}
