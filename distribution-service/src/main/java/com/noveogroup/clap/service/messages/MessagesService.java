package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.model.message.MessageDTO;

public interface MessagesService {
    void saveMessage(long revisionTimestamp, MessageDTO messageDTO);
}
