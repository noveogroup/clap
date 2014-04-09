package com.noveogroup.clap.converter;

import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.model.message.CrashMessage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * @author Andrey Sokolov
 */
public class MessagesConverter {

    private static final Mapper MAPPER = new DozerBeanMapper();

    public CrashMessage map(MessageEntity messageEntity){
        return MAPPER.map(messageEntity,CrashMessage.class);
    }

    public MessageEntity map(CrashMessage message){
        return MAPPER.map(message,MessageEntity.class);
    }
}
