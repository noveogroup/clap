package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.model.message.BaseMessage;

import java.io.File;
import java.io.InputStream;

public interface MessagesService {
    void saveMessage(String variantHash, BaseMessage message);

    void saveMessage(String variantHash, BaseMessage message, InputStream inputStream);

    File getScreenshot(long messageId);

    <T extends BaseMessage> T getMessage(long messageId,Class<T> messsageClass);
}
