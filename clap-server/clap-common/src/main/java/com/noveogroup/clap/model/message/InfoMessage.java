package com.noveogroup.clap.model.message;

/**
 * @author Andrey Sokolov
 */
public class InfoMessage extends BaseMessage {
    @Override
    public MessageType type() {
        return MessageType.INFO;
    }
}
