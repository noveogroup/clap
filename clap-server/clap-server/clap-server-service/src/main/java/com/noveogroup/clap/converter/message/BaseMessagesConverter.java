package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.converter.BaseConverter;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.entity.message.log.LogEntryEntity;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.log.LogEntry;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseMessagesConverter extends BaseConverter<BaseMessage,BaseMessageEntity>{
    private static final Mapper MAPPER = new DozerBeanMapper();

    public void map(final BaseMessageEntity entity, final BaseMessage map){
        super.map(map,entity);
        map.setDeviceId(entity.getDeviceId());
        final Date date = entity.getTimestamp();
        if(date != null){
            map.setTimestamp(date.getTime());
        }
        mapDeviceInfo(entity,map);
    }

    protected void mapLogs(final List<LogEntryEntity> logs, final List<LogEntry> map) {
        if (logs != null) {
            for (LogEntryEntity logEntryEntity : logs) {
                map.add(MAPPER.map(logEntryEntity, LogEntry.class));
            }
        }
    }

    protected void mapLogCat(final List<String> logs, final List<String> map) {
        if (logs != null) {
            map.addAll(logs);
        }
    }

    protected void mapDeviceInfo(final BaseMessageEntity messageEntity, final BaseMessage map) {
        map.setDeviceInfo(new HashMap<String, String>());
        final Map<String, String> deviceInfo = messageEntity.getDeviceInfo();
        if (deviceInfo != null) {
            map.getDeviceInfo().putAll(deviceInfo);
        }
    }

}
