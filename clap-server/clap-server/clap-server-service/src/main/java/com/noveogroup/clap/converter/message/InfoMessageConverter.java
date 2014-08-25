package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.InfoMessageEntity;
import com.noveogroup.clap.model.message.InfoMessage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * @author Andrey Sokolov
 */
public class InfoMessageConverter implements OneTypeMessagesConverter<InfoMessage,InfoMessageEntity> {
    private static final Mapper MAPPER = new DozerBeanMapper();
    @Override
    public InfoMessage map(final InfoMessageEntity messageEntity, final ConfigBean configBean) {
        return MAPPER.map(messageEntity, InfoMessage.class);
    }

    @Override
    public InfoMessageEntity map(final InfoMessage message) {
        return MAPPER.map(message, InfoMessageEntity.class);
    }

    @Override
    public Class<InfoMessage> getMessageClass() {
        return InfoMessage.class;
    }

    @Override
    public Class<InfoMessageEntity> getMessageEntityClass() {
        return InfoMessageEntity.class;
    }
}
