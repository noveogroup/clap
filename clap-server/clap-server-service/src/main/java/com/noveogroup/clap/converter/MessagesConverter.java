package com.noveogroup.clap.converter;

import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.model.message.Message;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * @author Andrey Sokolov
 */
public class MessagesConverter {

    private static final Mapper MAPPER = new DozerBeanMapper();

    public Message map(MessageEntity messageEntity){
        return MAPPER.map(messageEntity,Message.class);
    }

    public MessageEntity map(Message message){
        return MAPPER.map(message,MessageEntity.class);
    }
}
