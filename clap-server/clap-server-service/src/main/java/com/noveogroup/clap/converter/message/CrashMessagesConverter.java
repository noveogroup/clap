package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.entity.message.CrashMessageEntity;
import com.noveogroup.clap.model.message.CrashMessage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * @author Andrey Sokolov
 */
public class CrashMessagesConverter implements OneTypeMessagesConverter<CrashMessage, CrashMessageEntity> {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Override
    public CrashMessage map(CrashMessageEntity messageEntity) {
        return MAPPER.map(messageEntity, CrashMessage.class);
    }

    @Override
    public CrashMessageEntity map(CrashMessage message) {
        return MAPPER.map(message, CrashMessageEntity.class);
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
