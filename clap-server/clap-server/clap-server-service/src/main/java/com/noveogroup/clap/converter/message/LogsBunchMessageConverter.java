package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.LogsBunchMessageEntity;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.log.LogEntry;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessageConverter extends BaseMessagesConverter
        implements OneTypeMessagesConverter<LogsBunchMessage, LogsBunchMessageEntity> {

    private static final Mapper MAPPER = new DozerBeanMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(LogsBunchMessageConverter.class);

    @Override
    public LogsBunchMessage map(final LogsBunchMessageEntity messageEntity, final ConfigBean configBean) {
        LOGGER.debug("message entity - " + messageEntity);
        final LogsBunchMessage map = new LogsBunchMessage();
        map(messageEntity,map);
        return map;
    }


    public LogsBunchMessage mapFullInfo(final LogsBunchMessageEntity messageEntity, final ConfigBean configBean){
        final LogsBunchMessage map = map(messageEntity,configBean);
        mapDeviceInfo(messageEntity,map);
        map.setLogCat(new ArrayList<String>());
        mapLogCat(messageEntity.getLogCat(), map.getLogCat());
        map.setLogs(new ArrayList<LogEntry>());
        mapLogs(messageEntity.getLogs(), map.getLogs());
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
