package com.noveogroup.clap.converter.message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.CrashMessageEntity;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.ThreadInfo;
import com.noveogroup.clap.model.message.log.LogEntry;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class CrashMessagesConverter extends BaseMessagesConverter
        implements OneTypeMessagesConverter<CrashMessage, CrashMessageEntity> {

    private static final Mapper MAPPER = new DozerBeanMapper();
    private Type listType = new TypeToken<List<ThreadInfo>>() {
    }.getType();
    private Gson gson = new Gson();

    @Override
    public CrashMessage map(CrashMessageEntity messageEntity, final ConfigBean configBean) {
        final CrashMessage map = new CrashMessage();
        map(messageEntity,map);
        map.setThreadId(messageEntity.getThreadId());
        map.setException(messageEntity.getException());
        return map;
    }


    @Override
    public CrashMessageEntity map(CrashMessage message) {
        final CrashMessageEntity map = MAPPER.map(message, CrashMessageEntity.class);
        map.setThreadsInfoJSON(gson.toJson(message.getThreads(), listType));
        return map;
    }

    public CrashMessage mapFullInfo(CrashMessageEntity messageEntity, final ConfigBean configBean){
        final CrashMessage map = map(messageEntity,configBean);
        mapDeviceInfo(messageEntity,map);
        map.setLogCat(new ArrayList<String>());
        mapLogCat(messageEntity.getLogCat(), map.getLogCat());

        map.setLogs(new ArrayList<LogEntry>());
        mapLogs(messageEntity.getLogs(), map.getLogs());

        final Object fromJson = gson.fromJson(messageEntity.getThreadsInfoJSON(), listType);
        if (fromJson instanceof List) {
            map.setThreads((List<ThreadInfo>) fromJson);
        }
        return map;
    }

    @Override
    public Class<CrashMessage> getMessageClass() {
        return CrashMessage.class;
    }

    @Override
    public Class<CrashMessageEntity> getMessageEntityClass() {
        return CrashMessageEntity.class;
    }
}
