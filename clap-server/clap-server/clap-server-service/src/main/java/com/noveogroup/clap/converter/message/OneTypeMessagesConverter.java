package com.noveogroup.clap.converter.message;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.entity.message.BaseMessageEntity;
import com.noveogroup.clap.model.message.BaseMessage;

/**
 * @author Andrey Sokolov
 */
public interface OneTypeMessagesConverter<T extends BaseMessage, V extends BaseMessageEntity> {

    T map(V messageEntity,ConfigBean configBean);

    T mapFullInfo(V messageEntity,ConfigBean configBean);

    V map(T message);

    Class<T> getMessageClass();
    Class<V> getMessageEntityClass();

}
