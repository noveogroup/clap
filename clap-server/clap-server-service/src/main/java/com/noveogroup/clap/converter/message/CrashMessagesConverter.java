package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.CrashMessageEntity;
import com.noveogroup.clap.model.message.CrashMessage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CrashMessagesConverter implements OneTypeMessagesConverter<CrashMessage, CrashMessageEntity> {

    private static final Mapper MAPPER = new DozerBeanMapper();

    @Override
    public CrashMessage map(CrashMessageEntity messageEntity,final ConfigBean configBean) {
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
