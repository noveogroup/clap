package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.model.message.Message;

public interface MessagesService {
    void saveMessage(long revisionTimestamp, Message message);
}
