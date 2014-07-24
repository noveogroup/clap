package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.model.message.BaseMessage;

import java.io.File;
import java.io.InputStream;

public interface MessagesService {
    void saveMessage(String revisionHash, BaseMessage message);

    void saveMessage(String revisionHash, BaseMessage message, InputStream inputStream);

    File getScreenshot(long messageId);
}
