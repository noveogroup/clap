package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.LogsBunchMessageEntity;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessageConverter implements OneTypeMessagesConverter<LogsBunchMessage,LogsBunchMessageEntity> {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Override
    public LogsBunchMessage map(final LogsBunchMessageEntity messageEntity, final ConfigBean configBean) {
        final LogsBunchMessage map = MAPPER.map(messageEntity, LogsBunchMessage.class);
        map.getLogCat().addAll(messageEntity.getLogCat());
        return map;
    }

    @Override
    public LogsBunchMessageEntity map(final LogsBunchMessage message) {
        return MAPPER.map(message, LogsBunchMessageEntity.class);
    }

    @Override
    public Class<LogsBunchMessage> getMessageClass() {
        return LogsBunchMessage.class;
    }

    @Override
    public Class<LogsBunchMessageEntity> getMessageEntityClass() {
        return LogsBunchMessageEntity.class;
    }
}
