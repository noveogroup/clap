package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.CrashMessage;

public interface MessagesService {
    void saveMessage(String revisionHash, BaseMessage message);
}
